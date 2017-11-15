package com.unown.finalunown;

import android.content.Context;
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
import android.widget.ListView;
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
    private ArrayList<Seller> listSellers;
    private List<Product> listOfProducts;
    private DatabaseReference mDatabase, sellerDB, nameDatabase;
    private double locationLat, locationLong;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context mContext = this;
        setContentView(R.layout.activity_user_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listView1);

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
       // listOfSellers = new ArrayList<Seller>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        listOfSellers = new ArrayList<Seller>();
        ReadData();
        /*while (listOfSellers.isEmpty()){
            Toast.makeText(mContext, "the list is empty in on create", Toast.LENGTH_SHORT).show();
        }
        //still trying to fix this because the info from the firebase is having issues
        //because it's asynchronous so I need a way to look at it only after is has loaded....I think
        listAdapter adapter = new listAdapter(mContext, listOfSellers);
        if (!listOfSellers.isEmpty()) {
            Toast.makeText(mContext, "from list loaded: " + listOfSellers.get(0).getName(), Toast.LENGTH_SHORT).show();
        }
        lv.setAdapter(adapter);
        */



    }

    public void ReadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        listSellers = new ArrayList<Seller>();
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
                    Seller mSeller = new Seller(listOfProducts,name, userDescription, Double.valueOf(locationLat), Double.valueOf(locationLong), Double.valueOf(totalSales), Integer.valueOf(numberSales), seller );
                    mSeller.setName(name);
                    mSeller.setLocationLatitude(Double.valueOf(locationLat));
                    mSeller.setLocationLongitude(Double.valueOf(locationLong));
                    mSeller.setSeller(seller);
                    listSellers.add(mSeller);
                    Toast.makeText(UserSearchActivity.this,"mSeller.getName()" + mSeller.getName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserSearchActivity.this, "name " + name, Toast.LENGTH_SHORT).show();

                    //Seller mSeller = new Seller();
                    Toast.makeText(UserSearchActivity.this, String.valueOf(postSnapshot.child("name").getValue()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(UserSearchActivity.this, postSnapshot.getValue(Seller.class).getName(), Toast.LENGTH_SHORT).show();
                }

                listAdapter adapter = new listAdapter(UserSearchActivity.this, listSellers);
                lv.setAdapter(adapter);
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
