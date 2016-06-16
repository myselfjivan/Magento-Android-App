package in.co.mrfoody.mrfoody.ui.userActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import in.co.mrfoody.mrfoody.R;
import in.co.mrfoody.mrfoody.Service.MrFoodyApplicationConfigurationKeys;

/**
 * Created by om on 13/6/16.
 */
public class newRegistration extends Fragment {
    String TAG = "Registration Activity";
    Button btn;
    TextView firstname;
    TextView lastname;
    TextView email;
    TextView password;
    EditText link;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_new_registration,container,false);
        btn = (Button) v.findViewById(R.id.btn_login);
        firstname = (TextView) v.findViewById(R.id.input_name);
        lastname = (TextView) v.findViewById(R.id.input_last_name);
        email = (TextView) v.findViewById(R.id.input_email);
        password = (TextView) v.findViewById(R.id.input_password);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            return;
        }
        btn.setEnabled(false);

        new catalogProductListAsyncTask().execute();
    }

    public class catalogProductListAsyncTask extends AsyncTask<String, String, String> {
        public String METHOD = null;
        ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
        private ProgressDialog pdia;

        String firstName = firstname.getText().toString();
        String lastName = lastname.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("input details", firstName + lastName + emailString + passwordString);
        }

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            env.dotNet = false;
            env.xsd = SoapSerializationEnvelope.XSD;
            env.enc = SoapSerializationEnvelope.ENC;

            METHOD = "customerCustomerCreate";
            SoapObject request = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, METHOD);

            SoapObject user = new SoapObject(MrFoodyApplicationConfigurationKeys.NAMESPACE, "customerData");
            user.addProperty("email", emailString);
            user.addProperty("firstname", firstName);
            user.addProperty("lastname", lastName);
            user.addProperty("passwotd", passwordString);
            user.addProperty("website_id", 1);
            user.addProperty("store_id", 1);
            user.addProperty("group_id", 1);

            request.addProperty("username", MrFoodyApplicationConfigurationKeys.USERNAME);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("apiKey", MrFoodyApplicationConfigurationKeys.APIUSERKEY);
            request.addProperty("customerData", user);
            request.addProperty("sessionId", MrFoodyApplicationConfigurationKeys.sessionId);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MrFoodyApplicationConfigurationKeys.URL);
            env.setOutputSoapObject(request);

            try {
                androidHttpTransport.call("", env, headerPropertyArrayList);
                Object userId = env.getResponse();
                Log.d("", userId.toString());
                Toast.makeText(getContext(), "Registration Responce:" + userId.toString(), Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstname.getText().toString();
        String lastName = lastname.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstname.setError("at least 3 characters");
            valid = false;
        } else {
            firstname.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            lastname.setError("at least 3 characters");
            valid = false;
        } else {
            lastname.setError(null);
        }

        if (emailString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordString.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
