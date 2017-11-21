package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    static final int TAKE_PHOTO_PERMISSION = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int PICK_IMAGE_REQUEST = 3;
    private DatabaseReference mDatabase;
    User mUser;


    ImageView profilePicture;
    Button takePictureButton;
    Button saveButton;
    EditText nameET, locationET, descriptionET;

    Uri file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //Intent passedIntent = getIntent();


        //initialize fields
        saveButton = (Button) findViewById(R.id.saveButton);
        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        nameET = (EditText) findViewById(R.id.nameEditText);
        locationET = (EditText) findViewById(R.id.locationEditText);
        descriptionET = (EditText) findViewById(R.id.descriptionEditText);
        //ImageView??
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        //get passed info from previous activity
        String nameStr = getIntent().getStringExtra("MY_NAME");
        String descriptionStr = getIntent().getStringExtra("MY_DESCRIPTION");
        String locationStr = getIntent().getStringExtra("MY_LOCATION");

        //populate fields with profile info
        nameET.setText(nameStr);
        descriptionET.setText(descriptionStr);
        locationET.setText(locationStr);

        // We are giving you the code that checks for permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, TAKE_PHOTO_PERMISSION);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                String name = nameET.getText().toString();
                String location = locationET.getText().toString();
                String description = descriptionET.getText().toString();
                //ImageView???

                //update item
                //mUser.setName(name);
                //mUser.setDescription(description);
                //mUser.setLocationLatitude();
                //mUser.setLocationLongitude();

                //return new item to previous activity
                Intent returnIntent = new Intent();
                returnIntent.putExtra("MY_NAME2", name);
                returnIntent.putExtra("MY_LOCATION2", location);
                returnIntent.putExtra("MY_DESCRIPTION2", description);
                //get newly inputted strings and .set them for the user


                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // This is called when permissions are either granted or not for the app
        // You do not need to edit this code.

        if (requestCode == TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        // Add code here to start the process of taking a picture
        // Note you can send an intent to the camera to take a picture...
        // So start by considering that!
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    public void getImageFromLibrary(View view) {
        // Add code here to start the process of getting a picture from the library
        Intent intent = new Intent();
        //Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Add code here to handle results from both taking a picture or pulling
        // from the image gallery.

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if(resultCode == RESULT_OK) {
                profilePicture.setImageURI(file);
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST) {
            //Add here.
            if(resultCode == RESULT_OK && data != null && data.getData() != null){
                Uri uri = data.getData();
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);
                    profilePicture.setImageBitmap(bitmap);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //If cancel button is pressed, go back to main activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}
