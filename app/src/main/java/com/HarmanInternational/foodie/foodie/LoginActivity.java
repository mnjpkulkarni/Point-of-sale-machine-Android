package com.HarmanInternational.foodie.foodie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText createPassword,confirmPassword,enterPassword,setPassword;
    String crPassword,coPassword;
    Button createAccount,login,set;
    ContentResolver contentResolver;
    String PASSWORD="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createPassword=(EditText)findViewById(R.id.create_password);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        createAccount=(Button)findViewById(R.id.create_account);
        enterPassword=(EditText)findViewById(R.id.enter_password);
        login=(Button)findViewById(R.id.login);
        set=(Button)findViewById(R.id.set);
        setPassword=(EditText)findViewById(R.id.set_password);

        createPassword.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);
        createAccount.setVisibility(View.GONE);

        contentResolver=getContentResolver();

        String[] projection = new String[]{"password1", "password2"};
        Cursor cursor = contentResolver.query(DBAdapter.CONTENT_URL, projection, null, null, null);
        if(cursor.moveToFirst()){

            do{

                PASSWORD = cursor.getString(cursor.getColumnIndex("password1"));

            }while (cursor.moveToNext());

        }


        if(PASSWORD.equals(""))
        {

            setPassword.setVisibility(View.VISIBLE);
            set.setVisibility(View.VISIBLE);
            enterPassword.setVisibility(View.GONE);
            login.setVisibility(View.GONE);
        }

        else
        {   enterPassword.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            setPassword.setVisibility(View.GONE);
            set.setVisibility(View.GONE);
        }

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PASSWORD=setPassword.getText().toString();
                ContentValues contentValues=new ContentValues();
                contentValues.put(DBAdapter.CREATE_PASSWORD,PASSWORD);
                contentValues.put(DBAdapter.CONFIRM_PASSWORD,PASSWORD);
                Uri uri = getContentResolver().insert(DBAdapter.CONTENT_URL, contentValues);
                Toast.makeText(getApplicationContext(),"Password created",Toast.LENGTH_LONG).show();
                setPassword.setVisibility(View.GONE);
                set.setVisibility(View.GONE);
                enterPassword.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);


            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crPassword=createPassword.getText().toString();
                coPassword=confirmPassword.getText().toString();

                if(crPassword.equals(PASSWORD))
                {
                    PASSWORD=coPassword;

                    ContentValues contentValues=new ContentValues();
                    contentValues.put(DBAdapter.CREATE_PASSWORD,PASSWORD);
                    contentValues.put(DBAdapter.CONFIRM_PASSWORD,PASSWORD);
                    Uri uri = getContentResolver().insert(DBAdapter.CONTENT_URL, contentValues);
                    Toast.makeText(getApplicationContext(),"Password changed",Toast.LENGTH_LONG).show();

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pswd=enterPassword.getText().toString();
                if(pswd.equals(PASSWORD))
                {
                    Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoginActivity.this,PasswordActivity.class);
                    startActivity(i);

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivity.this,OrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

    }



}

