package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    User mUser;
    TextView name, location, description;
    private ArrayList<User> listOfUsers;
    private DatabaseReference mDatabase, userDB, nameDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        description = (TextView) findViewById(R.id.descriptionTextView);

        //attempt to populate profile with user info
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    //change this so that 'edit' button only appears if it si the current user's profile
    //https://stackoverflow.com/questions/9994967/android-how-to-make-a-button-display-on-a-condition

    public void editProfile(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
