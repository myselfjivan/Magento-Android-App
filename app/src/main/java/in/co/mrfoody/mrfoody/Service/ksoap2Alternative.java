package in.co.mrfoody.mrfoody.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created by om on 12/6/16.
 */
public class ksoap2Alternative extends AsyncTask<String, Void, String>
{
    private static ProgressDialog dialog;

    private static int ReadTimeout = 10000;
    private static int ConnectTimeout = 10000;
    private static String urlString = "http://dev.mrfoody.co.in/api/v2_soap/";
    private static String namespace = "urn:Magento";
    private static String soapAction = namespace +"/";
    private String[] cacheParams;
    private Context context;

    public ksoap2Alternative(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        try {
            if(dialog != null && dialog.isShowing())
                dialog.dismiss();
            dialog = ProgressDialog.show(context, "", "Loading...", false);
        }
        catch (Exception ignored)
        {}
    }

    @Override
    protected String doInBackground(String... Params)
    {
        try {
            cacheParams = Params;

            // Create soap message
            StringBuilder sb = new StringBuilder();
            sb.append("<v:Envelope xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\"><v:Body><")
                    .append(Params[0])
                    .append(" xmlns=\"")
                    .append(namespace)
                    .append("\">");
            for (String param : Params) {
                String[] parameter_data = param.split("\\|", 2);
                if (parameter_data.length == 2) {
                    sb.append("<")
                            .append(parameter_data[0])
                            .append(">")
                            .append(parameter_data[1])
                            .append("</")
                            .append(parameter_data[0])
                            .append(">");
                }
            }
            sb.append("</")
                    .append(Params[0])
                    .append("></v:Body></v:Envelope>");
            String content = sb.toString();

            // create post for soap message
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // this needs to be HttpsURLConnection if you are using ssl
            connection.setReadTimeout(ReadTimeout);
            connection.setConnectTimeout(ConnectTimeout);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
            connection.setRequestProperty("Accept-Encoding", "gzip"); // comment this line out if you dont want to compress all service calls
            connection.setRequestProperty("SOAPAction", soapAction + Params[0]);

            // Get soap response
            OutputStream os = connection.getOutputStream();
            os.write(content.getBytes("UTF-8"));
            os.flush();
            os.close();
            String response = "";
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                br = connection.getContentEncoding().equals("gzip") ?
                        new BufferedReader(new InputStreamReader(getUnZippedInputStream(connection.getInputStream()))) :
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                throw new Exception("HTTP ERROR: " + responseCode);
            }

            // Extract data
            int index = response.indexOf(Params[0] + "Result>") + 7+Params[0].length();
            response = response.substring(index, response.indexOf("</"+Params[0], index));

            // release all resources
            br.close();
            connection.disconnect();

            return response;
        }
        catch (Exception ex)
        {
            return ex.getClass().toString();
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        Log.d("result",""+result);
        try
        {
            if(result.equals("class java.net.ConnectException") || result.equals("class java.net.UnknownHostException") || result.equals("class java.net.SocketTimeoutException"))
            {
                //you can ask the user if they would like to retry as there was a network error
                //use the cacheParams to recall the method
            }
        }
        finally
        {
            try {
                dialog.dismiss();
            }
            catch (Exception ignored)
            {}
        }
    }

    //gzip compression
    private GZIPInputStream getUnZippedInputStream(InputStream inputStream) throws IOException {
            /* workaround for Android 2.3
               (see http://stackoverflow.com/questions/5131016/)
            */
        try {
            return (GZIPInputStream)inputStream;
        } catch (ClassCastException e) {
            return new GZIPInputStream(inputStream);
        }
    }
}