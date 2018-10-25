package com.example.ashishshrivastav.exasecure;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.ashishshrivastav.exasecure.Login.FileName;

public class Home extends AppCompatActivity {
    Button button,button1;
    TextView about;
   // DatabaseHelper db=new DatabaseHelper(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkUser();
        button=(Button)findViewById(R.id.button);
        button1=(Button)findViewById(R.id.button1);
        TextView about=(TextView)findViewById(R.id.about);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
    }
    public void openActivity()
    {
        Intent intent=new Intent(this,Signup.class);
        startActivity(intent);
    }
    public void openActivity2()
    {
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
    }
    public void openActivity3()
    {
        Intent intent=new Intent(this,About.class);
        startActivity(intent);
    }
    public void checkUser(){
        SharedPreferences sharedPref = getSharedPreferences( FileName,Context.MODE_PRIVATE);
        String s=sharedPref.getString("Data",null);
        Intent introIntent = new Intent(this,Portal.class);
        if (s!=null) {
            startActivity(introIntent);
        }
    }
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
