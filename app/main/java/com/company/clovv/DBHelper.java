package com.company.clovv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "CartData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table cartDetails(productName TEXT primary key," +
                "productPrice number," +
                "shopIdData TEXT," +
                "qValData TEXT," +
                "productType TEXT," +
                "userWtType TEXT," +
                "productUnitPrice TEXT,"+
                "shopPerWtType TEXT,"+
                "productId TEXT,"+
                "productStockWtType TEXT,"+
                "totalStock)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists cartDetails");

    }

    public boolean insertCartData(String productName, double pricePro, String shopIdData, String qValData,
                                  String productType,String userWtType,
                                  String proUnitPrice,
                                  String shopPerWtType,
                                  String productId,
                                  String productStockWtType,
                                  String totalStock){
        SQLiteDatabase db = this.getWritableDatabase();

        //check database
        db.execSQL("create table if not exists cartDetails (productName TEXT primary key," +
                "productPrice number," +
                "shopIdData TEXT," +
                "qValData TEXT," +
                "productType TEXT," +
                "userWtType TEXT," +
                "productUnitPrice TEXT,"+
                "shopPerWtType TEXT,"+
                "productId TEXT,"+
                "productStockWtType TEXT,"+
                "totalStock)");





        ContentValues contentValues = new ContentValues();
        contentValues.put("productName",productName);
        contentValues.put("productPrice",pricePro);
        contentValues.put("shopIdData",shopIdData);
        contentValues.put("qValData",qValData);
        contentValues.put("productType",productType);
        contentValues.put("userWtType",userWtType);
        contentValues.put("productUnitPrice",proUnitPrice);
        contentValues.put("shopPerWtType",shopPerWtType);
        contentValues.put("productId",productId);
        contentValues.put("productStockWtType",productStockWtType);
        contentValues.put("totalStock",totalStock);

        long result = db.insert("cartDetails",null,contentValues);
        if (result==-1){
            return false;
        }else {
            return true;
        }

    }

    public Boolean updateCartData(String productNameData,String pricePro,String qValData,String userWtType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productPrice",pricePro);
        contentValues.put("qValData",qValData);
        contentValues.put("userWtType",userWtType);

        Cursor cursor = db.rawQuery("Select * from cartDetails where productName=?",new String[] {productNameData});

        if (cursor.getCount()>0){

            long result = db.update("cartDetails",contentValues,"productName=?",new String[] {productNameData});
            if (result==-1){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }



    }

    public void deleteAllCart(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop Table if exists cartDetails");




    }

    public Boolean deleteCartItem(String productName){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from cartDetails where productName=?", new String[]{productName});
        if (cursor.getCount()>0){
            long result = db.delete("cartDetails","productName=?",new String[]{productName});
            if (result==-1){
                return false;
            }else {
                return true;
            }
        } else {
            return false;
        }

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("create table if not exists cartDetails (productName TEXT primary key," +
                "productPrice number," +
                "shopIdData TEXT," +
                "qValData TEXT," +
                "productType TEXT," +
                "userWtType TEXT," +
                "productUnitPrice TEXT,"+
                "shopPerWtType TEXT,"+
                "productId TEXT,"+
                "productStockWtType TEXT,"+
                "totalStock)");



        Cursor cursor = db.rawQuery("Select * from cartDetails",null);

        return cursor;


    }

    public Cursor getShopId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from cartDetails",null);
        return cursor;
    }

}
