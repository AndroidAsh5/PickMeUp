package com.lenovo.test;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    Context context;
SharedPreferences sharedPreferences;
private String name;

    public String getName() {
        name=sharedPreferences.getString("userdata","");

        return name;
    }

    public void setName(String name) {
        this.name = name;
    sharedPreferences.edit().putString("userdata",name).commit();

    }

    public User(Context context){

this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);

    }


};