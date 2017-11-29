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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductSearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ArrayList<Product> listOfProducts;
    private DatabaseReference mDatabase, sellerDB, nameDatabase, inventoryDB;
    //private double locationLat, locationLong;
    private String location;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context mContext = this;
        setContentView(R.layout.activity_product_search);
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
        listOfProducts = new ArrayList<Product>();
        ReadData();



    }

    public void ReadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        listOfProducts = new ArrayList<Product>();
        sellerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(ProductSearchActivity.this, "count: " + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {


                    String name = String.valueOf(postSnapshot.child("name").getValue());
                    nameDatabase = sellerDB.child(name);
                    inventoryDB = nameDatabase.child("Inventory");
                    Toast.makeText(ProductSearchActivity.this, name, Toast.LENGTH_SHORT).show();

                    inventoryDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                String productName = String.valueOf(postSnapshot.getKey());
                                Toast.makeText(ProductSearchActivity.this, productName, Toast.LENGTH_SHORT).show();
                                String productCategory = String.valueOf(postSnapshot.child("Category").getValue());
                                String price = String.valueOf(postSnapshot.child("Price").getValue());
                                String quantity = String.valueOf(postSnapshot.child("Quantity").getValue());
                                String owner = String.valueOf(postSnapshot.child("Owner").getValue());
                                Product newProd = new Product( productCategory, Double.valueOf(price), productName, Integer.valueOf(quantity), owner);
                                listOfProducts.add(newProd);
                            }
                            //TODO: change this so that we get the passed username of whoever logs in
                            listAdapterProductsSell adapter = new listAdapterProductsSell(ProductSearchActivity.this, listOfProducts, "kyle");
                            lv.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError firebaseError) {
                            Log.e("The read failed: " ,firebaseError.getMessage());
                        }
                    });

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