package com.lenovo.test.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Carpool.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "user";
public static final String UID="Id";
    public static final String MID="MemberId";
public static final String MNAME="MemberName";
public static final String CID="CarpoolId";
    public static final String CREATE_TABLE="CREATE TABLE " +TABLE_NAME+ "("
+ UID+ " INTEGER PRIMARY KEY AUTOINCREMENT," + MID +" TEXT,"
+ MNAME+ " TEXT,"+ CID+" TEXT"+")";
            public static final String DROP_TABLE="DROP TABLE IF EXISTS " + TABLE_NAME;




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE);
onCreate(db);
    }
};