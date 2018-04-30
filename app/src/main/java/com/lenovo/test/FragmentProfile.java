package com.lenovo.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentProfile extends Fragment {
    TextView textView,textView2,textView3,textView4;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String name,email,username,num;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = getActivity().getSharedPreferences("pref_key", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name", "");
        email=sharedPreferences.getString("EmailId", "");
        username=sharedPreferences.getString("UserName", "");
        num=sharedPreferences.getString("MobileNo", "");


textView= (TextView) v.findViewById(R.id.tv);
        textView2= (TextView) v.findViewById(R.id.username);
        textView3= (TextView) v.findViewById(R.id.email);
        textView4= (TextView) v.findViewById(R.id.num);
        textView.setText(name);
        //textView2.setText(username);
      //  textView3.setText(email);
       // textView4.setText(num);
        return v;


    }
};