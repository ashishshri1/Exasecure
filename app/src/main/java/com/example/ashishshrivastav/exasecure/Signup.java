package com.example.ashishshrivastav.exasecure;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    Button button1;
    EditText et,et1,et2,et3,et4;
    TextView textView;
    DatabaseHelper mydb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mydb = new DatabaseHelper(this);
        button1=(Button)findViewById(R.id.button1);
        et=(EditText)findViewById(R.id.et);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        textView=(TextView)findViewById(R.id.textView);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
    }
    public void openActivity()
    {
        int f1=0,f2=1,f3=0,f4=0,f5=0;
        String phone=et1.getText().toString();
        if (phone.length()!=10)
            f1=1;
        String email=et2.getText().toString();
        if(email.length()<11)
            f2=1;
        else
        {
            String subemail=email.substring(email.length()-10,email.length());
            if(subemail.equals("@gmail.com"))
                f2=0;
        }
        if(et.getText().toString().length()==0)
            f3=1;
        if(et3.getText().toString().length()<=5)
            f4=1;
        if(et4.getText().toString().length()<=5)
            f5=1;
        if(f1==1)
        {
            textView.setText("Fill a valid Phone number");
            return;
        }
        if(f2==1)
        {
            textView.setText("Fill a valid gmail id");
            return;
        }
        if(f3==1)
        {
            textView.setText("Enter Name");
            return;
        }
        if(f4==1)
        {
            textView.setText("Select a username more than 5 characters");
            return;
        }
        if(f5==1)
        {
            textView.setText("Select a Password more than 5 characters");
            return;
        }
        SQLiteDatabase db1=mydb.getReadableDatabase();
        Cursor c = db1.rawQuery("SELECT ID FROM user where username=?",new String[]{et3.getText().toString()});
        if(c.moveToFirst())
        {
            textView.setText("Username not available");
            c.close();
            db1.close();
            return;
        }
            c.close();
            db1.close();
            SQLiteDatabase db=mydb.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("Name",et.getText().toString());
            values.put("Phone",et1.getText().toString());
            values.put("Email",et2.getText().toString());
            values.put("Username",et3.getText().toString());
            values.put("Password",et4.getText().toString());
            long row=db.insert("User",null,values);
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
    }
}
