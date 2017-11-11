package com.unown.finalunown;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText passwordEditText;
    EditText usernameEditText;
    EditText locationEditText;
    RadioButton isSellerButton;
    RadioButton notSellerButton;
    User mUser;
    Button registerButton;
    ArrayList<Product> listOfRecent;
    ArrayList<Product> cart;
    ArrayList<Product> inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        listOfRecent = new ArrayList<>();
        cart = new ArrayList<>();
        inventory = new ArrayList<>();
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        isSellerButton = (RadioButton) findViewById(R.id.yesSellerRadioButton);
        notSellerButton = (RadioButton) findViewById(R.id.noSellerRadioButton);

        /*if (isSellerButton.isChecked()){
            Toast.makeText(this, "is Seller button checked", Toast.LENGTH_SHORT).show();
            mUser = new Buyer(cart, "","",0.0,0.0,listOfRecent,false);
        }
        if (notSellerButton.isChecked()){
            Toast.makeText(this, "not sell er button checked", Toast.LENGTH_SHORT).show();
            mUser = new Seller(inventory,"","", 0.0,0.0,0,0,true);
        }
        */
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    public void writeNewUser(View view) {
        if (isSellerButton.isChecked()){
            Toast.makeText(this, "is Seller button checked", Toast.LENGTH_SHORT).show();
            mUser = new Buyer(cart, "","",0.0,0.0,listOfRecent,true);
        }
        if (notSellerButton.isChecked()){
            Toast.makeText(this, "not seller button checked", Toast.LENGTH_SHORT).show();
            mUser = new Seller(inventory,"","", 0.0,0.0,0,0,false);
        }
        mUser.setName(usernameEditText.getText().toString());
        Toast.makeText(this, mUser.getName(), Toast.LENGTH_SHORT).show();
        if(mUser.isSeller){
            mDatabase.child("Seller").child(mUser.getName()).setValue(mUser);
        } else {
            mDatabase.child("Buyer").child(mUser.getName()).setValue(mUser);
        }
    }

}
