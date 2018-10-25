package com.example.ashishshrivastav.exasecure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button button1;
    EditText et3,et4,et5;
    TextView textView;
    public final static String FileName="Portal";
    DatabaseHelper db=new DatabaseHelper(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button1=(Button)findViewById(R.id.button1);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        et5=(EditText)findViewById(R.id.et5);
        textView=(TextView)findViewById(R.id.textView);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function();
            }
        });
    }
    public void function()
    {
        String username=et3.getText().toString();
        String password=et4.getText().toString();
        String key=et5.getText().toString();
        SQLiteDatabase db1=db.getReadableDatabase();
        Cursor c=db1.rawQuery("Select ID from user where username=?",new String[]{username});
        if(!c.moveToFirst())
        {
            textView.setText("Username Invalid");
            c.close();
            return;
        }
        c.close();
        Cursor d=db1.rawQuery("Select password from user where username=?",new String[]{username});
        d.moveToFirst();
        String pass=d.getString(0);
        if(!pass.equals(password))
        {
            textView.setText("Wrong Password");
            d.close();
            return;
        }
        d.close();
        db1.close();
        if(et5.length()!=4)
        {
            textView.setText("Enter 4 digit key");
            return;
        }
        String data=username+" "+password+" "+key;
        SharedPreferences sharedPref = getSharedPreferences(FileName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Data",data);
        editor.apply();
        Intent intent=new Intent(this,Portal.class);
        intent.putExtra("Key",key);
        intent.putExtra("Username",username);
        intent.putExtra("Password",password);
        startActivity(intent);
        //finish();
    }

}
