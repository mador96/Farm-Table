package com.unown.finalunown;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefsFile";
    public static final String PREFS_NAME2 = "SellerStatusFile";

    private DatabaseReference mDatabase;
    EditText passwordEditText;
    EditText usernameEditText;
    EditText locationEditText;
    RadioButton isSellerButton;
    RadioButton notSellerButton;
    User mUser;
    Button registerButton;
    List<Product> listOfRecent;
    List<Product> cart;
    List<Product> inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences credentials = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences sellerStatus = getSharedPreferences(PREFS_NAME2, 0);

        listOfRecent = new ArrayList<>();
        cart = new ArrayList<>();
        inventory = new ArrayList<Product>();
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        isSellerButton = (RadioButton) findViewById(R.id.yesSellerRadioButton);
        notSellerButton = (RadioButton) findViewById(R.id.noSellerRadioButton);
        Product apple = new Product("fruit", 0.30, "apple");
        Product carrot = new Product("vegetable", 0.45, "carrot");
        //inventory.add(apple);
        //inventory.add(carrot);
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    public void writeNewUser(View view) {
        String usernameValue = usernameEditText.getText().toString();
        String passwordValue = passwordEditText.getText().toString();
        SharedPreferences sellerStatus = getSharedPreferences(PREFS_NAME2, 0);
        String sellerYesNo = null;

        //prep for password existence check
        String existingPassword = null;
        SharedPreferences credentials = getSharedPreferences(PREFS_NAME, 0);
        existingPassword = credentials.getString(usernameValue, null);

        //make sure user entered something into both fields
        if(usernameValue.matches("") || passwordValue.matches("")){
            Toast.makeText(this, "You must enter a username and password", Toast.LENGTH_LONG).show();
        }
        //make sure user isn't registering a username that is already in use
        else if(existingPassword != null){ //If given username already exists
            Toast.makeText(this, "This username already exists!", Toast.LENGTH_LONG).show();
        }
        //make sure users can't register without selecting a radio button
        else if(!isSellerButton.isChecked() && !notSellerButton.isChecked()){
            Toast.makeText(this, "You must specify if you want to register as a seller", Toast.LENGTH_LONG).show();
        }

        //Save credentials and seller status to SharedPreferences
        else{
            credentials = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = credentials.edit();
            editor.putString(usernameValue, passwordValue);
            editor.commit();

            if (notSellerButton.isChecked()){
                Toast.makeText(this, "not Seller button checked", Toast.LENGTH_SHORT).show();
                mUser = new Buyer(cart, "","",0.0,0.0,listOfRecent,false);
                sellerYesNo = "Yes";
            }
            if (isSellerButton.isChecked()){
                Toast.makeText(this, "is seller button checked", Toast.LENGTH_SHORT).show();
                mUser = new Seller(inventory,"","", 0.0,0.0,0,0,true);
                sellerYesNo = "No";

            }

            sellerStatus = getSharedPreferences(PREFS_NAME2, 0);
            SharedPreferences.Editor editor2 = sellerStatus.edit();
            editor2.putString(usernameValue, sellerYesNo);
            editor2.commit();
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show(); //remove

            mUser.setName(usernameEditText.getText().toString());
            Toast.makeText(this, mUser.getName(), Toast.LENGTH_SHORT).show();
            if(mUser.isSeller){
                mDatabase.child("Seller").child(mUser.getName()).setValue(mUser);
            } else {
                mDatabase.child("Buyer").child(mUser.getName()).setValue(mUser);
            }
        }

        Intent intent = new Intent(this, UserSearchActivity.class);
        startActivity(intent);

    }



}
