package com.example.ashishshrivastav.exasecure;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.ashishshrivastav.exasecure.Login.FileName;
public class Know_account extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Button logout,submit;
    TextView name, email;
    Spinner spinner;
    String []names;
    int id;
    DatabaseHelper db=new DatabaseHelper(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_account);
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
        submit=(Button)findViewById(R.id.sub);
        SharedPreferences SP = getSharedPreferences(FileName, 0);
        String data=SP.getString("Data", null);
        String username="",password="",key="";
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
        Cursor c=db1.rawQuery("Select name,email from user where username=?",new String[]{username});
        c.moveToFirst();
        name.setText(c.getString(0));
        email.setText(c.getString(1));
        c.close();
        Cursor e=db1.rawQuery("Select id from user where username= ?",new String[]{username});
        e.moveToFirst();
        id=e.getInt(0);
        e.close();
        Cursor d=db1.rawQuery("Select count(*) from account where id=?",new String[]{Integer.toString(id)});
        d.moveToFirst();
        int count=d.getInt(0);
        d.close();
        String []names=new String[count];
        Cursor f=db1.rawQuery("Select name from account where id=?",new String[]{Integer.toString(id)});
        f.moveToFirst();
        names[0]=f.getString(0);
        int index=1;
        while(f.moveToNext())
        {
            names[index++]=f.getString(0);
        }
        f.close();
        db1.close();
        spinner =(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Know_account.this,android.R.layout.simple_list_item_1,names);
        spinner.setAdapter(adapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                Intent intent=new Intent(Know_account.this,Details.class);
                intent.putExtra("Id",id);
                intent.putExtra("name",text);
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