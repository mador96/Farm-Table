package com.unown.finalunown;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Seller> listOfSellers;
    private DatabaseReference mDatabase, sellerDB, nameDatabase;
    private double locationLat, locationLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // this is my code below above is just stuff for having the slideout
        listOfSellers = new ArrayList<Seller>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");

        ReadData();

    }

    public void ReadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        sellerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(UserSearchActivity.this, "count: " + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                   // Toast.makeText(UserSearchActivity.this, String.valueOf(postSnapshot.getValue()), Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserSearchActivity.this, String.valueOf(postSnapshot.child("locationLatitude").getValue()), Toast.LENGTH_SHORT).show();
                    String locationLat = String.valueOf(postSnapshot.child("locationLatitude").getValue());
                    String locationLong = String.valueOf(postSnapshot.child("locationLongitude").getValue());
                    String name = String.valueOf(postSnapshot.child("name").getValue());
                    boolean seller = (boolean) postSnapshot.child("seller").getValue();
                    String numberSales = String.valueOf(postSnapshot.child("numberSales").getValue());
                    String totalSales = String.valueOf(postSnapshot.child("totalSales").getValue());
                    String userDescription = String.valueOf(postSnapshot.child("description").getValue());

                    //Seller mSeller = new Seller();
                    Toast.makeText(UserSearchActivity.this, String.valueOf(postSnapshot.child("name").getValue()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(UserSearchActivity.this, postSnapshot.getValue(Seller.class).getName(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
