package com.example.ashishshrivastav.exasecure;

import android.content.ContentValues;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.ashishshrivastav.exasecure.Login.FileName;

public class Change_Key extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    TextView name, email,t1;
    EditText et1;
    Button change;
    DatabaseHelper db=new DatabaseHelper(this);
    int id;
    String username="",password="",key="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__key);
        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        name=(TextView)header.findViewById(R.id.Name);
        email=(TextView)header.findViewById(R.id.email);
        t1=(TextView)findViewById(R.id.t1);
        et1=(EditText)findViewById(R.id.et1);
        change=(Button)findViewById(R.id.change);
        SharedPreferences SP = getSharedPreferences(FileName, 0);
        String data=SP.getString("Data", null);
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
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nkey=et1.getText().toString();
                if(nkey.length()!=4)
                {
                    t1.setText("Enter a 4 digit key");
                    return;
                }
                int newkey=Integer.parseInt(nkey);
                String data=username+" "+password+" "+newkey;
                SharedPreferences sharedPref = getSharedPreferences(FileName, 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Data",data);
                editor.apply();
                SQLiteDatabase mydb=db.getReadableDatabase();
                Cursor c=mydb.rawQuery("select count(*) from account where id=?",new String[]{Integer.toString(id)});
                c.moveToFirst();
                int count=c.getInt(0);
                c.close();
                String names[]=new String[count];
                String euser[]=new String[count];
                String epass[]=new String[count];
                Cursor d=mydb.rawQuery("Select name,username,password from account where id=?",new String[]{Integer.toString(id)});
                d.moveToFirst();
                names[0]=d.getString(0);
                euser[0]=d.getString(1);
                epass[0]=d.getString(2);
                int index=1;
                while(d.moveToNext())
                {
                    names[index]=d.getString(0);
                    euser[index]=d.getString(1);
                    epass[index]=d.getString(2);
                    index++;
                }
                d.close();
                String user[]=new String[count];
                String pass[]=new String[count];
                for(int j=0;j<count;j++) {
                    int length = euser[j].length();
                    int arr[] = new int[length];
                    int ke = Integer.parseInt(key) % 94;
                    for (int i = 0; i < length; i++)
                        arr[i] = (euser[j].charAt(i) - 33 - ke + 94) % 94 + 33;
                    user[j]="";
                    for (int i = 0; i < length; i++)
                        user[j] += (char) (arr[i]);
                }
                for(int j=0;j<count;j++) {
                    int length = epass[j].length();
                    int arr[] = new int[length];
                    int ke = Integer.parseInt(key) % 94;
                    for (int i = 0; i < length; i++)
                        arr[i] = (epass[j].charAt(i) - 33 - ke + 94) % 94 + 33;
                    pass[j]="";
                    for (int i = 0; i < length; i++)
                        pass[j]+= (char)(arr[i]);
                }
                for(int j=0;j<count;j++)
                {
                    int length1=user[j].length();
                    int arr[]=new int[length1];
                    for(int i=0;i<length1;i++)
                        arr[i]=((user[j].charAt(i)-33+newkey)%94)+33;
                    euser[j]="";
                    for(int i=0;i<length1;i++)
                        euser[j]+=(char)(arr[i]);
                }
                for(int j=0;j<count;j++)
                {
                    int length1=pass[j].length();
                    int arr[]=new int[length1];
                    for(int i=0;i<length1;i++)
                        arr[i]=((pass[j].charAt(i)-33+newkey)%94)+33;
                    epass[j]="";
                    for(int i=0;i<length1;i++)
                        epass[j]+=(char)(arr[i]);
                }
                SQLiteDatabase newdb=db.getWritableDatabase();
                newdb.execSQL("delete from account where id=?",new String[]{Integer.toString(id)});
                for(int j=0;j<count;j++) {
                    ContentValues values = new ContentValues();
                    values.put("Id", id);
                    values.put("name",names[j]);
                    values.put("username", euser[j]);
                    values.put("password",epass[j]);
                    long row = newdb.insert("Account", null, values);
                }
                Toast toast = Toast.makeText(getApplicationContext(), "Key Changed", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(Change_Key.this,Portal.class);
                startActivity(intent);
            }
        });
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
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
}