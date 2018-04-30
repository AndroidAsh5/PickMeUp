package com.lenovo.test;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.test.Helper.DatabaseHelper;
import com.lenovo.test.activities.FindRideList;
import com.lenovo.test.apiConfiguration.ApiConfiguration;
import com.lenovo.test.httpRequsetProcessor.HttpRequestProcessor;
import com.lenovo.test.httpRequsetProcessor.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentSearch extends Fragment {

    private EditText source, pooldate;
    private Button button;
    private ApiConfiguration apiConfiguration;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private String baseURL, urlRegister;
    private String c, cc;
    private String jsonPostString, jsonResponseString;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;
    ArrayList<DriverBean> driverBeans;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    DriverBean bean;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String member_id;

    private static final String TAG = "MySearch";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //  Toast.makeText(getActivity(), "dv", Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.find_ride, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        source = (EditText) v.findViewById(R.id.sourcee);
        pooldate = (EditText) v.findViewById(R.id.date);
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


        button = (Button) v.findViewById(R.id.find);
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        baseURL = apiConfiguration.getApi();
        urlRegister = baseURL + "CarPoolRegistrationAPI/SearchCarPoolApplication";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (source.getText().toString().isEmpty()) {

                    source.setError("Please Enter Source");

                } else if (pooldate.getText().toString().isEmpty()) {


                    pooldate.setError("Please Enter journey date");


                } else {
//                    sharedPreferences = getActivity().getSharedPreferences("pref_key", Context.MODE_PRIVATE);
//                    member_id = sharedPreferences.getString("ApplicationUserId", "");
//                    // Log.d(TAG, member_id);
                    c = source.getText().toString().trim();
                    cc = pooldate.getText().toString().trim();

                    new Search().execute(c, cc);
                }
            }
        });
        return v;
    }

    private class Search extends AsyncTask<String, String, String> {
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
            c = params[0];
            cc = params[1];
            //Log.e("Name", name);

            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("Source", c);
                jsonObject.put("PoolDate", cc);
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
            //   Log.d(TAG, s);


            driverBeans = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("responseData");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jObject = jsonArray.getJSONObject(i);
                        //    Toast.makeText(getActivity(), jObject.getString("Destination"), Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(getActivity(), jObject.getString("Charges"), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), jObject.getString("SeatsAvailable"), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getActivity(), jObject.getString("Time"), Toast.LENGTH_SHORT).show();
                        // driverBean = new DriverBean(jObject.getString("Pooldate"), jObject.getString("Source"), jObject.getString("Destination"), jObject.getInt("Charges"), jObject.getInt("SeatsAvailable"), jObject.getString("MemberName"));

                        String date = jObject.getString("PoolDate");
                        String source = jObject.getString("Source");
                        String dest = jObject.getString("Destination");
                        String charge = jObject.getString("Charges");
                        String avail = jObject.getString("SeatsAvailable");
                        String mId = jObject.getString("MemberId");
                        String cId = jObject.getString("Id");
                        String mname = jObject.getString("MemberName");

                        //    String name = jObject.getString("MemberName");
                        Log.d(TAG, mId);
                        Log.d(TAG, cId);
                        db = databaseHelper.getWritableDatabase();

                        ContentValues cv = new ContentValues();

                        cv.put(DatabaseHelper.MID, mId);
                        cv.put(DatabaseHelper.MNAME, mname);
                        cv.put(DatabaseHelper.CID, cId);

                        long id = db.insert(DatabaseHelper.TABLE_NAME, null, cv);
                        if (id < 0) {

                            Toast.makeText(getActivity(), "Insertion Unsuccessful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Insertion Successful", Toast.LENGTH_LONG).show();
                        }


                        bean = new DriverBean(date, source, dest, charge, avail, mId, cId);

                        driverBeans.add(bean);

//                        Log.d(TAG, driverBeans.toString());

                    }
                    passArrayList();
                } else {
                    Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
                }

            } catch (
                    JSONException e
                    )

            {
                e.printStackTrace();
            }


        }

    }

    private void passArrayList() {

        Intent intent = new Intent(getActivity(), FindRideList.class);
        // intent.putExtra("arraylist", driverBeans.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", driverBeans);
        Log.d(TAG, " Id is  " + bean.getMemberId());
        //  intent.putExtra("data",new DataWrapper(driverBeans));
        intent.putExtras(bundle);
        startActivity(intent);


    }


};