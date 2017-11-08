package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefsFile";
    EditText usernameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Load shared preferences
        SharedPreferences credentials = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = credentials.edit();
        editor.putString("admin", "admin"); //default admin login
        editor.commit();

        usernameET = (EditText) findViewById(R.id.usernameEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);
    }

    //Check if given credentials exist in the system..... how is this being called?
    public void loadFromSharedPreferences(View view){
        String passwordStr = null;
        SharedPreferences credentials = getSharedPreferences(PREFS_NAME, 0);
        String usernameValue = usernameET.getText().toString();
        String passwordValue = passwordET.getText().toString();
        passwordStr = credentials.getString(usernameValue, null);

        //If credentials don't have a match
        if(usernameValue == null || passwordStr == null){
            Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_LONG).show();
        }
        else{ //remove
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            //Bring to home page
        }
    }

    public void openRegistration(View view){ //start RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
