package com.unown.finalunown;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText passwordEditText;
    EditText usernameEditText;
    EditText locationEditText;
    Buyer myBuyer;
    Button registerButton;
    ArrayList<Product> listOfRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        listOfRecent = new ArrayList<>();
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        // locationEditText = (EditText)findViewById(R.id.locationEditText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myBuyer = new Buyer(usernameEditText.getText().toString(), "", listOfRecent);

    }

    public void writeNewUser(View view) {
        myBuyer = new Buyer(usernameEditText.getText().toString(), "", listOfRecent);
        Toast.makeText(this, myBuyer.getName(), Toast.LENGTH_SHORT).show();
            mDatabase.child(myBuyer.getName()).setValue(myBuyer);
    }

}
