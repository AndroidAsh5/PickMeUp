package com.lenovo.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.test.apiConfiguration.ApiConfiguration;
import com.lenovo.test.httpRequsetProcessor.HttpRequestProcessor;
import com.lenovo.test.httpRequsetProcessor.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private EditText edtName, edtPasswd;
    private Button btnLogin, btnRegister;
    private String name, passwd,userName2,num;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlLogin, jsonStringToPost, jsonResponseString;
    private boolean success;
    private String message, address, emailID, phone, password, userName, email, userId, errorMessage;
    private final String TAG = "A";
    SharedPreferences sharedPreferences;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);


//        if (user.getName() != "") {
//
//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);
//        } else {

        setTitle("Pick Me Up");
        edtName = (EditText) findViewById(R.id.namee);
        edtPasswd = (EditText) findViewById(R.id.pass);


        btnLogin = (Button) findViewById(R.id.login);
        // btnRegister= (Button) findViewById(R.id.register);
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        textView = (TextView) findViewById(R.id.textViewLinkRegister2);
        //Getting base url
        baseURL = apiConfiguration.getApi();
        urlLogin = baseURL + "AccountAPI/GetLoginUser";
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().isEmpty()) {
                    edtName.setError("Enter username");
                } else if (edtPasswd.getText().toString().isEmpty()) {
                    edtPasswd.setError("Enter password");
                } else {

                    name = edtName.getText().toString();
                    passwd = edtPasswd.getText().toString();
                    new LoginTask().execute(name, passwd);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }
    //   }

    public class LoginTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Wait...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            name = params[0];
            passwd = params[1];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", name);
                jsonObject.put("Password", passwd);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonStringToPost, urlLogin);
                jsonResponseString = response.getJsonResponseString();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            //Log.d("Response String", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                errorMessage = jsonObject.getString("ErrorMessage");
                // success = jsonObject.getBoolean("success");
                // Log.d("Success", String.valueOf(success));
                // errorMessage = jsonObject.getString("ErrorMessage");
                String erMsg = "User Authenticated!!";

                if (errorMessage.equals(erMsg)) {
                    userName = jsonObject.getString("Name");
                    email = jsonObject.getString("EmailId");
                    userId = jsonObject.getString("ApplicationUserId");
                    userName2=jsonObject.getString("UserName");
                    num=jsonObject.getString("MobileNo");
                    sharedPreferences = getSharedPreferences("pref_key", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Name", userName);
                    editor.putString("EmailId", email);
                    editor.putString("ApplicationUserId", userId);
                    editor.putString("UserName",userName2);
                    editor.putString("MobileNo",num);
                    editor.commit();

                    Log.d(TAG, userId);

                    edtName.setText(" ");
                    edtPasswd.setText(null);
                    Toast.makeText(LoginActivity.this, "Welcome " + userName, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
