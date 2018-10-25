package com.example.ashishshrivastav.exasecure;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.ashishshrivastav.exasecure.Login.FileName;

public class Add_Account extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Button submit;
    EditText et1,et2,et3;
    TextView name, email,warn;
    String username,password,key;
    DatabaseHelper db=new DatabaseHelper(this);
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__account);
        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer1);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navview1);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        name=(TextView)header.findViewById(R.id.Name);
        email=(TextView)header.findViewById(R.id.email);
        submit=(Button)findViewById(R.id.submit);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        warn=(TextView)findViewById(R.id.warn);
        SharedPreferences SP = getSharedPreferences(FileName, 0);
        String data=SP.getString("Data", null);
        username="";
        password="";
        key="";
        int pass=0,k=0;
        for(int i=0;i<data.length();i++)
        {
            if(data.charAt(i)==' ')
            {
                pass=i+1;
                break;
            }
            else{
                username+=data.charAt(i);
            }

        }
        for(int i=pass;i<data.length();i++)
        {
            if(data.charAt(i)==' ')
            {
                k=i+1;
                break;
            }
            else{
                password+=data.charAt(i);
            }

        }
        for(int i=k;i<data.length();i++)
        {
            key+=data.charAt(i);
        }
        SQLiteDatabase db1=db.getReadableDatabase();
        Cursor c=db1.rawQuery("Select name,email,id from user where username=?",new String[]{username});
        c.moveToFirst();
        name.setText(c.getString(0));
        email.setText(c.getString(1));
        id=c.getInt(2);
        c.close();
        db1.close();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(username,key);
            }
        });
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navview1);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item))
            return true;
        else
            return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPref = getSharedPreferences(FileName, 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Data", null);
                editor.apply();
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                return true;
            case R.id.myac:
                SQLiteDatabase mydb=db.getReadableDatabase();
                Cursor d=mydb.rawQuery("Select count(*) from account where id=?",new String[]{Integer.toString(id)});
                d.moveToFirst();
                if(d.getInt(0)==0)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "No accounts to show", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                Intent intent1 =new Intent(this,MyAccounts.class);
                intent1.putExtra("Id",id);
                startActivity(intent1);
                return true;
            case R.id.about:
                Intent intent2=new Intent(this,About.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void save(String username,String key)
    {
        SQLiteDatabase db2=db.getReadableDatabase();
        Cursor c=db2.rawQuery("Select id from user where username= ?",new String[]{username});
        c.moveToFirst();
        int id=c.getInt(0);
        c.close();
        String name=et1.getText().toString();
        String user=et2.getText().toString();
        String pass=et3.getText().toString();
        Cursor d=db2.rawQuery("Select count(*) from account where name=?",new String[]{name});
        d.moveToFirst();
        if(d.getInt(0)!=0)
        {
            warn.setText("Account already exists");
            return;
        }
        d.close();
        db2.close();
        int k=Integer.parseInt(key);
        int length1=user.length();
        int arr[]=new int[length1];
        for(int i=0;i<length1;i++)
            arr[i]=((user.charAt(i)-33+k)%94)+33;
        String euser="";
        for(int i=0;i<length1;i++)
            euser+=(char)(arr[i]);
        int length2=pass.length();
        int brr[]=new int[length2];
        for(int i=0;i<length2;i++)
            brr[i]=((pass.charAt(i)-33+k)%94)+33;
        String epass="";
        for(int i=0;i<length2;i++)
            epass+=(char)(brr[i]);
        if(name.length()==0)
        {
            warn.setText("Enter account name");
            return;
        }
        if(euser.length()==0)
        {
            warn.setText("Enter Account's Username");
            return;
        }
        if(epass.length()==0)
        {
            warn.setText("Enter Account's Password");
            return;
        }
        SQLiteDatabase db3=db.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Id",id);
        values.put("name",name);
        values.put("username",euser);
        values.put("password",epass);
        long row=db3.insert("Account",null,values);
        db3.close();
        Intent intent=new Intent(this,Portal.class);
        startActivity(intent);
    }
}