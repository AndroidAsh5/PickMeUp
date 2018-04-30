package com.lenovo.test;

import android.widget.Button;

import java.io.Serializable;

public class DriverBean implements Serializable {

    String pooldate;
    String source;
    String destination;
    String charges;
    String seats;
    Button b;
    String memberId;
    String carpoolId;
  //  String offered_by;

    public DriverBean(String pooldate, String source, String destination, String charges, String seats,String memberId,String carpoolId)
    {
        this.pooldate = pooldate;
        this.source = source;
        this.destination = destination;
        this.charges = charges;
        this.seats = seats;
        this.memberId=memberId;
        this.carpoolId=carpoolId;
        this.b=b;
   //     this.offered_by = offered_by;
    }

    public String getPooldate() {
        return pooldate;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getCharges() {
        return charges;
    }

    public String getSeats() {
        return seats;
    }

    public Button getB() {
        return b;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getCarpoolId() {
        return carpoolId;
    }

    // public String getOffered_by() {
     //   return offered_by;
    //}
};