package com.lenovo.test;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

    private ArrayList<DriverBean> parliaments;

    public DataWrapper(ArrayList<DriverBean> data) {
        this.parliaments = data;
    }

    public ArrayList<DriverBean> getParliaments() {
        return this.parliaments;
    }

}