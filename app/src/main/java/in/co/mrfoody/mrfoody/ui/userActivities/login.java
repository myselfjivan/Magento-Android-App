package in.co.mrfoody.mrfoody.ui.userActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.mrfoody.mrfoody.Library.Catalog.catalogCategory.catalogCategoryLevel;
import in.co.mrfoody.mrfoody.Library.Catalog.catalogProduct.catalogProductAttributeMediaInfo;
import in.co.mrfoody.mrfoody.Library.Catalog.catalogProduct.catalogProductList;
import in.co.mrfoody.mrfoody.R;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;

/**
 * Created by om on 9/6/16.
 */
public class login extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), registration.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            //onLoginFailed();
            return;
        }

        //_loginButton.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Login Method is not enabled, hang on!", Toast.LENGTH_SHORT).show();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

    }

    public class catalogProductListAsyncTask extends AsyncTask<String, String, String> {
        public String METHOD = null;
        ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
        public List<catalogCategoryLevel> catalogCategoryLevels = new ArrayList<catalogCategoryLevel>();
        public ArrayList<String> ar = new ArrayList<String>();
        private ProgressDialog pdia;
        public List<catalogProductList> catalogProductLists = new ArrayList<catalogProductList>();
        public ArrayList<String> mainViewProductsArrayList = new ArrayList<String>();
        public List<catalogProductAttributeMediaInfo> catalogProductAttributeMediaInfos = new ArrayList<catalogProductAttributeMediaInfo>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            //persons = new ArrayList<>();

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "masterapiLoginmodelCustomerlogin";
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            request.addProperty("user", "admin@chippa.in");
            request.addProperty("pass", "test123@");
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env, headerPropertyArrayList);
                Object catalogProductListObject = env.getResponse();
                Log.d("catalog Product List", catalogProductListObject.toString());

                SoapObject catalogProductEntityArray = (SoapObject) env.getResponse(); //get response
                try {
                    for (int i = 0; i < catalogProductEntityArray.getPropertyCount(); i++) {
                        catalogProductList data = new catalogProductList();
                        SoapObject catalogProductEntity = (SoapObject) catalogProductEntityArray.getProperty(i);
                        data.setProduct_id(catalogProductEntity.getProperty("product_id").toString());
                        data.setSku(catalogProductEntity.getProperty("sku").toString());
                        data.setName(catalogProductEntity.getProperty("name").toString());
                        data.setSet(catalogProductEntity.getProperty("set").toString());
                        data.setType(catalogProductEntity.getProperty("type").toString());
                        catalogProductLists.add(data);
                    }

                    for (int i = 0; i < catalogProductLists.size(); i++) {
                        catalogProductList catalogProductList = catalogProductLists.get(i);
                        //ar.add(catalogProductList.getProduct_id());
                        System.out.println("product Id : " + catalogProductList.getProduct_id() +
                                " Name :" + catalogProductList.getName());
                        //persons.add(new Person(catalogProductList.getName(), catalogProductList.getName(), R.drawable.emma));
                        mainViewProductsArrayList.add(catalogProductList.getName());
                        try {
                            request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, "catalogProductAttributeMediaList");
                            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);
                            request.addProperty("product", catalogProductList.getProduct_id());
                            env.setOutputSoapObject(request);
                            androidHttpTransport.call("", env, headerPropertyArrayList);
                            SoapObject catalogProductImageEntityArray = (SoapObject) env.getResponse();

                            for (int j = 0; j < catalogProductImageEntityArray.getPropertyCount(); j++) {
                                catalogProductAttributeMediaInfo data = new catalogProductAttributeMediaInfo();
                                SoapObject catalogProductImageEntity = (SoapObject) catalogProductImageEntityArray.getProperty(j);
                                data.setLabel(catalogProductImageEntity.getProperty("label").toString());
                                data.setUrl(catalogProductImageEntity.getProperty("url").toString());
                                catalogProductAttributeMediaInfos.add(data);

                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int j = 0; j < catalogProductAttributeMediaInfos.size(); j++) {
                        catalogProductAttributeMediaInfo catalogProductAttributeMediaInfo = catalogProductAttributeMediaInfos.get(j);
                        System.out.println("Url: " + catalogProductAttributeMediaInfo.getUrl());
                        //new updateMainViwProductList().execute(j);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
