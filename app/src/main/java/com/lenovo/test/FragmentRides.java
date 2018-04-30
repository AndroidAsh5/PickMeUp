package com.lenovo.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentRides extends Fragment {

Button b;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rides,container,false);
        button = (Button) view.findViewById(R.id.b1);

        b= (Button) view.findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferRide offerRide=new OfferRide();
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container,offerRide,"offer").addToBackStack(null).commit();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindRide nextFrag= new FindRide();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag,"searchhh")
                        .addToBackStack(null)
                        .commit();


//
            }
        });


        return view;
    }
};