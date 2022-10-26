package com.company.clovv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqLocDBHelper extends SQLiteOpenHelper {

    public SqLocDBHelper(Context context) {
        super(context, "Addresses.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table addressDetails(address TEXT primary key," +
                "locality Text," +
                "longitude number," +
                "latitude number)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop Table if exists addressDetails");

    }

    public boolean insertAddressData(String address,
                                     String locality,
                                     double longitude,
                                     double latitude){
        SQLiteDatabase db = this.getWritableDatabase();

        //check database
        db.execSQL("create table if not exists addressDetails(address TEXT primary key," +
                "locality Text," +
                "longitude number," +
                "latitude number)");





        ContentValues contentValues = new ContentValues();
        contentValues.put("address",address);
        contentValues.put("locality",locality);
        contentValues.put("longitude",longitude);
        contentValues.put("latitude",latitude);


        long result = db.insert("addressDetails",null,contentValues);
        if (result==-1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("create table if not exists addressDetails(address TEXT primary key," +
                "locality Text," +
                "longitude number," +
                "latitude number)");



        Cursor cursor = db.rawQuery("Select * from addressDetails",null);

        return cursor;


    }

}
