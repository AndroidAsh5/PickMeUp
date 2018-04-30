package com.lenovo.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.test.apiConfiguration.ApiConfiguration;
import com.lenovo.test.httpRequsetProcessor.HttpRequestProcessor;
import com.lenovo.test.httpRequsetProcessor.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtAddress, edtEmailID, edtPhone, edtUserName, edtPassword;
    Button btnRegister;
    private String name;
    private String address;
    private String emailID;
    private String phone;
    private String userName;
    private String password;
    private String baseURL, urlRegister;
    private ApiConfiguration apiConfiguration;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private String jsonPostString, jsonResponseString;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Pick Me Up");
        edtName = (EditText) findViewById(R.id.name);
        edtEmailID = (EditText) findViewById(R.id.email);
        edtAddress = (EditText) findViewById(R.id.address);
        edtPhone = (EditText) findViewById(R.id.number);
        edtUserName = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.registerr);
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        baseURL = apiConfiguration.getApi();
        urlRegister = baseURL + "AccountAPI/SaveApplicationUser";


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Getting values..



                if(edtName.getText().toString().isEmpty()){

                    edtName.setError("Please Enter name");

                }else if(edtAddress.getText().toString().isEmpty()) {
                   edtAddress.setError("Please Enter adress");
                }else if(! edtEmailID.getText().toString().matches(emailPattern)) {


                    edtEmailID.setError("Please Enter Valid email");
                }else if(edtPhone.getText().toString().length()!=10) {

                    edtPhone.setError("Please Enter Mobile NUmber");
                }else if(edtUserName.getText().toString().isEmpty()) {

                    edtUserName.setError("Please enter Username");

                }else if(edtPassword.getText().toString().isEmpty()) {

                    edtPassword.setError("Please enter password");
                }
                    else{

                    name = edtName.getText().toString().trim();
                    address = edtAddress.getText().toString().trim();
                    emailID = edtEmailID.getText().toString();
                    phone = edtPhone.getText().toString();


                    userName = edtUserName.getText().toString();


                    password = edtPassword.getText().toString();
                new RegistrationTask().execute(name, address, emailID, phone, userName, password);

            }}


        });
    }

    public void click(View view) {

        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

    private class RegistrationTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Wait...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            name = params[0];
            address = params[1];
            //Log.e("Name", name);
            emailID = params[2];
            phone = params[3];
            userName = params[4];
            password = params[5];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Name", name);
                jsonObject.put("Address", address);
                jsonObject.put("EmailId", emailID);
                jsonObject.put("Phone", phone);
                jsonObject.put("UserName", userName);
                jsonObject.put("Password", password);

                jsonPostString = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonPostString, urlRegister);
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
            Log.d("Response String", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                int responseData = jsonObject.getInt("responseData");
                if (responseData == 1) {
                    Toast.makeText(RegisterActivity.this, "User registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else if (responseData == 2) {
                    Toast.makeText(RegisterActivity.this, "User Already exists.Please Try again...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Some unexpected error.Please try Again...", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    }

