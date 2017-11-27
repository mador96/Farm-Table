package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import com.google.firebase.firebase_core.*;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivity extends AppCompatActivity {
    User mUser;
    TextView name, location, description;
    String nameStr, locationStr, descriptionStr;
    private ArrayList<Seller> listOfSellers;
    private ArrayList<Buyer> listOfBuyers;
    private DatabaseReference mDatabase, theDB;
    String passingUsername = null;
    public static final String PREFS_NAME2 = "SellerStatusFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent passedIntent = getIntent();
        String nameString =  passedIntent.getStringExtra("name");

        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        description = (TextView) findViewById(R.id.descriptionTextView);

        //get username from login activity
        final String myUsername = getIntent().getStringExtra("MY_USERNAME");
        passingUsername = myUsername;

        //determine if this user is a buyer or a seller
        SharedPreferences sellerStatus = getSharedPreferences(PREFS_NAME2, 0);
        String status = sellerStatus.getString(passingUsername, null);

        //if user is a seller, get seller database
        if(status.equals("Yes")){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            theDB = mDatabase.child("Seller");
            listOfSellers = new ArrayList<Seller>();
        }
        //if user is a buyer, get buyer database
        else{
            mDatabase = FirebaseDatabase.getInstance().getReference();
            theDB = mDatabase.child("Buyer");
            listOfBuyers = new ArrayList<Buyer>();
        }


        theDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()) {
                   if(myUsername.equals(String.valueOf(postSnapshot.child("username").getValue()))) { //if person's username == username in the database
                       //get the info associated with that username
                       name.setText(String.valueOf(postSnapshot.child("name").getValue()));
                       nameStr = String.valueOf(postSnapshot.child("name").getValue());
                       description.setText(String.valueOf(postSnapshot.child("description").getValue()));  //this is messing up format?
                       descriptionStr = String.valueOf(postSnapshot.child("description").getValue());
                       location.setText(String.valueOf(postSnapshot.child("location").getValue()));
                       locationStr = String.valueOf(postSnapshot.child("location").getValue());
                       //How to get parent of the node i located so i can get the rest of the info?

                   }
               }
            }


            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

    }

    //change this so that 'edit' button only appears if it si the current user's profile
    //https://stackoverflow.com/questions/9994967/android-how-to-make-a-button-display-on-a-condition

    public void editProfile(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("MY_NAME", nameStr);
        intent.putExtra("MY_DESCRIPTION", descriptionStr);
        intent.putExtra("MY_LOCATION", locationStr);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) { //editProfile results
                final String returnedName = data.getStringExtra("MY_NAME2");
                final String returnedDescription = data.getStringExtra("MY_DESCRIPTION2");
                final String returnedLocation = data.getStringExtra("MY_LOCATION2");
                name.setText(returnedName);
                location.setText(returnedLocation);
                description.setText(returnedDescription);
                //how to set image? and store that image in firebase?

                //how to update firebase
                theDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()) {
                            if(passingUsername.equals(String.valueOf(postSnapshot.child("username").getValue()))) { //if person's username == username in the database
                                //then do something
                                 String parentNode = postSnapshot.getKey(); //this is the parent node
                                theDB.child(parentNode).child("name").setValue(returnedName);
                                theDB.child(parentNode).child("location").setValue(returnedLocation);
                                theDB.child(parentNode).child("description").setValue(returnedDescription);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.e("The read failed: " ,firebaseError.getMessage());
                    }
                });
            }
        }
    }
}
