package com.example.ashishshrivastav.exasecure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="account.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table User (ID integer primary key autoincrement,Name text,Phone text,Email text,Username text,Password text )");
       db.execSQL("create table Account (Id integer,name text,username text,password text,primary key(Id,name))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table if exists User");
        db.execSQL("DROP table if exists Account");
        onCreate(db);
    }
}
