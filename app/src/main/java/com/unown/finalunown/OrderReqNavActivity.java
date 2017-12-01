package com.unown.finalunown;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderReqNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    String ownerUsername;
    ArrayList<Product> myOrders;
    listAdapterProducts adapter;
    String descriptionString;

    private DatabaseReference mDatabase, buyerDB, userDB, cartDB, ownerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_req_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Farm Table: Orders");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list = (ListView) findViewById(R.id.orderListView);


        Intent passedIntent = getIntent();
        ownerUsername = passedIntent.getStringExtra("MY_USERNAME");

        readData();

        adapter = new listAdapterProducts(OrderReqNavActivity.this, myOrders);
        list.setAdapter(adapter);



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

       // builder.setTitle("Confirm");
       // builder.setMessage("Would you like to complete or remove this order?");


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){
                final int myPosition = position;

                String ownerString =  myOrders.get(myPosition).getOwner();
                buyerDB = mDatabase.child("Buyer");
                ownerDB = buyerDB.child(ownerString);
                //Toast.makeText(this, "username in place order" + username, Toast.LENGTH_SHORT).show();
                //get references to the cart of the buyer and pantry of seller so they can be modified
                //d
                //final int quantityRequested = order.getQuantity();
                //productName = order.getProductName();
                ownerDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        descriptionString = dataSnapshot.child("description").getValue().toString();

                        //Toast.makeText(OrderReqNavActivity.this, "inside ondatchanged " + descriptionString, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                builder.setMessage("Buyer Contact: " + descriptionString);
                builder.setTitle("Complete/Remove this order?");


                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        deleteFromFirebase(myOrders.get(myPosition).getProductName(), myOrders.get(myPosition).getOwner());
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }


    public void readData(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ownerDB = mDatabase.child("Order").child(ownerUsername);
        myOrders = new ArrayList<Product>();

        ownerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //myOrders.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    //add items
                    String buyerName = String.valueOf(postSnapshot.getKey()); //this gets buyer's username
                    for(DataSnapshot ps: postSnapshot.getChildren()){
                        String productName = String.valueOf(ps.getKey());
                        String category = String.valueOf(ps.child("Category").getValue());
                        String productPrice = String.valueOf(ps.child("Price").getValue());
                        String quantity = String.valueOf(ps.child("Quantity").getValue());

                        try {
                            Product newProduct = new Product(category, Double.parseDouble(productPrice), productName, Integer.parseInt(quantity), buyerName);
                            //replace owner name with buyer name
                            myOrders.add(newProduct);
                        }
                        catch(NumberFormatException e){
                            //Just using this to catch null error
                        }
                    }
                }
                adapter = new listAdapterProducts(OrderReqNavActivity.this, myOrders);
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    public void deleteFromFirebase(String productName, String buyerName){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ownerDB = mDatabase.child("Order").child(ownerUsername);
        final String pN = productName;
        myOrders = new ArrayList<Product>();

        ownerDB.child(buyerName).child(productName).setValue(null);
        //adapter.notifyDataSetChanged();

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
        getMenuInflater().inflate(R.menu.order_req_nav, menu);
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

        if (id == R.id.nav_pantry) {
            // Handle the camera action
            Intent intent = new Intent(this, PantryNavActivity.class);
            //intent.putExtra("MY_USERNAME", currentUser);//pass name of current user
            intent.putExtra("MY_USERNAME", ownerUsername);//pass name of current user
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, SellerProfileActivity.class);
            intent.putExtra("MY_USERNAME", ownerUsername);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(this, OrderReqNavActivity.class);
            intent.putExtra("MY_USERNAME", ownerUsername);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
