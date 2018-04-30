
package com.lenovo.test.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lenovo.test.DataWrapper;
import com.lenovo.test.DriverBean;
import com.lenovo.test.R;
import com.lenovo.test.RecyclerAdapter;

import java.sql.Driver;
import java.util.ArrayList;

public class FindRideList extends AppCompatActivity {
    private static final String TAG = "Helloo";
    private ArrayList<DriverBean> o;
RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    DriverBean d;
    String dd,s,dest,avail,charges,m,c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride_list);
      d=new DriverBean(dd,s,dest,avail,charges,m,c);
       // o=new ArrayList<>();
       // if(data!=null){
         //   DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("data");
           // Log.d(TAG, dw.toString());
Bundle bundleObject=getIntent().getExtras();
        o= (ArrayList<DriverBean>) bundleObject.getSerializable("data");
        Log.d(TAG, " "+dd);
recyclerView= (RecyclerView) findViewById(R.id.rv);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerAdapter=new RecyclerAdapter(this,o);
        recyclerView.setAdapter(recyclerAdapter);
//recyclerAdapter.getArrayList();


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//      if(data!=null){
//          DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("data");
//          Log.d(TAG, String.valueOf(dw));
//         // ArrayList<DriverBean> list = dw.getParliaments();
//
////        o= (ArrayList<DriverBean>) getIntent().getSerializableExtra("arraylist");
////        Log.d(TAG, String.valueOf(o));
//}
}}
