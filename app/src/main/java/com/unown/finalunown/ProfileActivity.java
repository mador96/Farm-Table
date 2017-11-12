package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    User mUser;
    TextView name, location, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        description = (TextView) findViewById(R.id.descriptionTextView);

       // name.setText(mUser.getName());
        //location.setText("A PLACE"); //fix this!!!
        //description.setText(mUser.getDescription());
    }
    public void editProfile(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
