package com.lenovo.test;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lenovo.test.apiConfiguration.ApiConfiguration;
import com.lenovo.test.httpRequsetProcessor.HttpRequestProcessor;
import com.lenovo.test.httpRequsetProcessor.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class OfferRide extends Fragment {

    private EditText source, destination, time, charges, seat;
    TextInputEditText pooldate;
    Button carpool;
    private ApiConfiguration apiConfiguration;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private String baseURL, urlRegister;
    private String mId, ss, d, p, t, c, s;
    private String jsonPostString, jsonResponseString;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;
    User user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String TAG = "Android";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ride_offer, container, false);
        user = new User(getActivity());
        source = (EditText) v.findViewById(R.id.source);
        destination = (EditText) v.findViewById(R.id.destination);
        pooldate = (TextInputEditText) v.findViewById(R.id.date);
        pooldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                pooldate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }


        });
        time = (EditText) v.findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c11 = Calendar.getInstance();
                mHour = c11.get(Calendar.HOUR_OF_DAY);
                mMinute = c11.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);


                timePickerDialog.show();

            }
        });


        charges = (EditText) v.findViewById(R.id.charges);
        seat = (EditText) v.findViewById(R.id.seat);
        seat.setFilters(new InputFilter[]{new InputFilterMinMax("1", "3")});
        carpool = (Button) v.findViewById(R.id.createcarpool);
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        baseURL = apiConfiguration.getApi();
        urlRegister = baseURL + "CarPoolRegistrationAPI/SaveCarPoolApplication";

        carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (source.getText().toString().isEmpty()) {
                    error = true;
                    source.setError("Please Enter source point");
                }
                if (destination.getText().toString().isEmpty()) {
                    error = true;
                    destination.setError("Please enter valid destination point");
                }
                if (pooldate.getText().toString().isEmpty()) {
                    error = true;
                    pooldate.setError("Please Enter journey date");
                }
                if (time.getText().toString().isEmpty()) {
                    error = true;
                    time.setError("Please enter journey time");
                }
                if (charges.getText().toString().isEmpty()) {
                    error = true;
                    charges.setError("Please enter charger per seat");
                }
                if (seat.getText().toString().isEmpty()) {
                    error = true;
                    seat.setError("Please enter no. of available seats");
                } else {
                    sharedPreferences = getActivity().getSharedPreferences("pref_key", Context.MODE_PRIVATE);
                    mId = sharedPreferences.getString("ApplicationUserId", "");
                    Log.d(TAG, mId);
                    ss = source.getText().toString().trim();
                    d = destination.getText().toString().trim();

                    p = pooldate.getText().toString().trim();

                    t = time.getText().toString().trim();
                    c = charges.getText().toString().trim();
                    s = seat.getText().toString().trim();
                    new CreateCarPool().execute(mId, ss, d, p, t, c, s);

                }
            }
        });


        return v;
    }


    private class CreateCarPool extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Wait...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            mId = params[0];
            ss = params[1];
            d = params[2];
            //Log.e("Name", name);
            p = params[3];
            t = params[4];
            c = params[5];
            s = params[6];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("MemberId", mId);
                jsonObject.put("Source", ss);
                jsonObject.put("Destination", d);
                jsonObject.put("PoolDate", p);
                jsonObject.put("Time", t);
                jsonObject.put("Charges", c);
                jsonObject.put("SeatsAvailable", s);
                jsonPostString = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonPostString, urlRegister);
                jsonResponseString = response.getJsonResponseString();
                //Log.d(TAG, mId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d(TAG, jsonPostString);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int responseData = jsonObject.getInt("responseData");
                if (responseData == 1) {
                    Toast.makeText(getActivity(), "Created", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                } else if (responseData == 2) {
                    Toast.makeText(getActivity(), "CarPool Already exists.Please Try again...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Some unexpected error.Please try Again...", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

};