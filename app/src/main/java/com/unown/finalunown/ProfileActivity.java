package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
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
    private DatabaseReference mDatabase, sellerDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        description = (TextView) findViewById(R.id.descriptionTextView);

        //see if i can get name of seller first
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB =  mDatabase.child("Seller");
        listOfSellers = new ArrayList<Seller>();
        final String myUsername = getIntent().getStringExtra("MY_USERNAME");



        sellerDB.addValueEventListener(new ValueEventListener() {
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
        startActivity(intent);
    }

    //why isn't this working??!?!?!?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //if(requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) { //editProfile results
                String test = data.getStringExtra("MY_NAME2");
                name.setText(test);
            }
        //}
    }
}
