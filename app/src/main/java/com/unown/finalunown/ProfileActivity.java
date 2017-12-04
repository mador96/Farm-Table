package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.google.firebase.firebase_core.*;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class ProfileActivity extends AppCompatActivity {
    User mUser;
    TextView name, location, description;
    String nameStr, locationStr, descriptionStr;
    ImageView profilePicture;
    private ArrayList<Seller> listOfSellers;
    private ArrayList<Buyer> listOfBuyers;
    private DatabaseReference mDatabase, theDB;
    String passingUsername = null;
    public static final String PREFS_NAME2 = "SellerStatusFile";
    private StorageReference storageRef;
    private FirebaseStorage storage;
    String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent passedIntent = getIntent();
        //String nameString =  passedIntent.getStringExtra("MY_USERNAME");

        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        description = (TextView) findViewById(R.id.descriptionTextView);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);

        //Firebase
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://final-project-final-unown.appspot.com");

        //get username from login activity
        final String currentUser = passedIntent.getStringExtra("MY_USERNAME");
        passingUsername = currentUser;
        Toast.makeText(this, passingUsername, Toast.LENGTH_SHORT).show();

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
                    if(currentUser.equals(String.valueOf(postSnapshot.child("username").getValue()))) { //if person's username == username in the database
                        //get the info associated with that username
                        name.setText(String.valueOf(postSnapshot.child("name").getValue()));
                        nameStr = String.valueOf(postSnapshot.child("name").getValue());
                        description.setText(String.valueOf(postSnapshot.child("description").getValue()));
                        descriptionStr = String.valueOf(postSnapshot.child("description").getValue());
                        location.setText(String.valueOf(postSnapshot.child("location").getValue()));
                        locationStr = String.valueOf(postSnapshot.child("location").getValue());
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

        //Load profile picture from firebase
        StorageReference myImage = storageRef.child(passingUsername).child(passingUsername + ".jpg");

        myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(getApplicationContext()).load(uri).into(profilePicture);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
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
        intent.putExtra("username", passingUsername);
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

                StorageReference myImage = storageRef.child(passingUsername).child(passingUsername + ".jpg");

                myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Glide.with(getApplicationContext()).load(uri).into(profilePicture);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });

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