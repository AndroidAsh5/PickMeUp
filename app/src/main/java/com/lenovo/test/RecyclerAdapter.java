package com.lenovo.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.test.apiConfiguration.ApiConfiguration;
import com.lenovo.test.httpRequsetProcessor.HttpRequestProcessor;
import com.lenovo.test.httpRequsetProcessor.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    public Context context;
    private static final String TAG = "CarId";
    public ArrayList<DriverBean> driver;
    public String mId, cId, datee;
    public ApiConfiguration apiConfiguration;
    public HttpRequestProcessor httpRequestProcessor;
    public Response response;
    public String jsonPostString, baseURL, jsonResponseString;
    public String urlSendRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public String memberId, carpoolId;
    public DriverBean driverBean;
    public String i1, i2, i3, i4, i5;
    //  private final ClickListener listener;
    public RecyclerAdapter(Context context, ArrayList<DriverBean> driver) {
        this.context = context;
        this.driver = driver;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        driverBean = driver.get(position);
        i1 = driverBean.getPooldate();
        i2 = driverBean.getSource();
        i3 = driverBean.getDestination();
        i4 = driverBean.getCharges();
        i5 = driverBean.getSeats();
        memberId = driverBean.getMemberId();
        carpoolId = driverBean.getCarpoolId();
        Log.d(TAG, " id's are =  :    " + memberId + carpoolId);
        holder.date.setText(i1);
        holder.source.setText(i2);
        holder.destination.setText(i3);
        holder.charges.setText(i4);
        holder.seatsAvailable.setText(i5);
holder.button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
if(v.getId()==R.id.bookRide) {
    driverBean = driver.get(position);
    String dd = driverBean.getDestination();
    Toast.makeText(context, "Destination : " + dd, Toast.LENGTH_SHORT).show();
    new SubmitRequest().execute(memberId, carpoolId, driverBean.getPooldate());

}

    }
});

    }

    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }

    public ArrayList<DriverBean> getArrayList() {
        return driver;
    }

    @Override
    public int getItemCount() {
        return driver.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date, source, destination, charges, seatsAvailable;
        private Button button;
        private RecyclerViewClickListener mListener;


        public MyViewHolder(View itemView) {
            super(itemView);

//            sharedPreferences = context.getSharedPreferences("pref_key", Context.MODE_PRIVATE);
//            memberId = sharedPreferences.getString("MemberId", "");
//            carpoolId=sharedPreferences.getString("CarPoolId", "");
//            Log.d(TAG, memberId);
//            Log.d(TAG, carpoolId);
//            // listenerRef = new WeakReference<>(listener);
            driverBean = new DriverBean(i1, i2, i3, i4, i5, memberId, carpoolId);
            // String g=driverBean.getCarpoolId();

 //           Log.d(TAG, "  Id's are =   " + memberId + carpoolId);
            date = (TextView) itemView.findViewById(R.id.textViewDate);
            //    Toast.makeText(context, "Available Rides", Toast.LENGTH_SHORT).show();
            source = (TextView) itemView.findViewById(R.id.textViewSource);
            destination = (TextView) itemView.findViewById(R.id.textViewDest);
            charges = (TextView) itemView.findViewById(R.id.textViewCharges);
            seatsAvailable = (TextView) itemView.findViewById(R.id.textViewSeats);
            button = (Button) itemView.findViewById(R.id.bookRide);
            httpRequestProcessor = new HttpRequestProcessor();
            response = new Response();
            apiConfiguration = new ApiConfiguration();
           // bookRide = (Button) itemView.findViewById(R.id.bookRide);
            baseURL = apiConfiguration.getApi();
            urlSendRequest = baseURL + "CarP/oolAssociation/SubmitCarPoolRequest";
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v.getId() == R.id.bookRide) {
//
//
//                        new SubmitRequest().execute(memberId, carpoolId, driverBean.getPooldate());
//
//
//                    }
//                }
//            });
        }

    }
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.bookRide) {
//                // Toast.makeText(v.getContext(),"Booked",Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Id   " + memberId + carpoolId);
//                new SubmitRequest().execute(memberId, carpoolId, driverBean.getPooldate());
//
//
//            }
//        }

        public  class SubmitRequest extends AsyncTask<String, String, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Wait...");
                progressDialog.show();


            }

            @Override
            protected String doInBackground(String... params) {
                memberId = params[0];

                Log.d(TAG, "Member id is : "+memberId);
                carpoolId = params[1];
                datee = params[2];

                //      Log.d(TAG, "id"+memberId+carpoolId);
                // d = params[2];
                //Log.e("Name", name);
                // p = params[3];
                // t = params[4];
                // c = params[5];
                // s = params[6];

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("MemberId", memberId);
                    jsonObject.put("CarPoolId", carpoolId);
                    jsonObject.put("DDate", datee);
                    jsonObject.put("Description", "");
                    //   Log.d(TAG, mId + cId); //jsonObject.put("Destination", d);
                    //  jsonObject.put("PoolDate", p);
                    //  jsonObject.put("Time", t);
                    //  jsonObject.put("Charges", c);
                    //  jsonObject.put("SeatsAvailable", s);
                    jsonPostString = jsonObject.toString();
                    response = httpRequestProcessor.pOSTRequestProcessor(jsonPostString, urlSendRequest);
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
                // Log.d(TAG, jsonPostString);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    //         JSONArray jsonArray = jsonObject.getJSONArray("responseData");
                    //       if (jsonArray.length() != 0) {
                    //         for (int i = 0; i < jsonArray.length(); i++) {
                    //           JSONObject jObject = js
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    // nArray.getJSONObject(i);
                    int responseData = jsonObject.getInt("responseData");
                    if (responseData == 1) {
                        Toast.makeText(context, "Booked", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else if (responseData == 2) {
                        Toast.makeText(context, "Request Already Sent...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Some unexpected error.Please try Again...", Toast.LENGTH_SHORT).show();
                    }
                    //   }
                    //        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }



