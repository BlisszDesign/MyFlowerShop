package com.example.android.myflowershop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FlowersDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = FlowersDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "webshop.db";
    private static final int DATABASE_VERSION = 1;

    public FlowersDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FLOWERS_TABLE = "CREATE TABLE " + FlowersContract.FlowersEntry.TABLE_NAME + "("
                + FlowersContract.FlowersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE + " INTEGER NOT NULL, "
                + FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + FlowersContract.FlowersEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT NOT NULL, "
                + FlowersContract.FlowersEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + FlowersContract.FlowersEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + FlowersContract.FlowersEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + FlowersContract.FlowersEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_FLOWERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}