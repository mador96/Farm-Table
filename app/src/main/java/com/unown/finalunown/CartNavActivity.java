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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private ArrayList<Product> cartItems;
    ListView list;
    Button placeOrderButton, clearCartButton;
    TextView totalCost;
    ArrayList<Product> myCart;
    String username;
    String ownertoReceive;
    listAdapterProducts adapter;
    String quantityString, productName;
    private DatabaseReference mDatabase, buyerDB, userDB, cartDB, ownerDB, sellerDB, pantryDB, productDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Farm Table: Cart");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list = (ListView) findViewById(android.R.id.list);
        placeOrderButton = (Button) findViewById(R.id.placeOrderButton);
        clearCartButton = (Button) findViewById(R.id.clearCart);
        totalCost = (TextView) findViewById(R.id.totalCost);

        /*
        //Delete later -- prepopulating to test
        myCart = new ArrayList<Product>();
        Product p1 = new Product("Fruit", 2.00, "Cherries", 5);
        Product p2 = new Product("Vegetable", 3.00, "Carrots", 5);
        Product p3 = new Product("Vegetable", 4.00, "Tomatoes",5);
        myCart.add(p1);
        myCart.add(p2);
        myCart.add(p3);
        //Delete later -- prepopulating to test
        */

        //Attempt to actually grab from cart:
        Intent passedIntent = getIntent();
        username = passedIntent.getStringExtra("MY_USERNAME");
        ReadData(); //need to return arraylist
        //Toast.makeText(this, myCart.size(), Toast.LENGTH_SHORT).show();
        adapter = new listAdapterProducts(CartNavActivity.this, myCart);
        list.setAdapter(adapter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Remove item from cart?");

        //allow user to click on items to delete from cart
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){
                final int myPosition = position;
                //
                //Toast.makeText(CartNavActivity.this, "inside clicker of list", Toast.LENGTH_SHORT).show();
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        deleteFromFirebase(myCart.get(myPosition).getProductName());
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
                //deleteFromFirebase(myCart.get(position).getProductName(), myCart.get(position).getOwner());
                //myCart.remove(position);
                // adapter.notifyDataSetChanged();
                AlertDialog alert = builder.create();
                alert.show();    //update cost!

            }

        });



    }


    public void clearMyCart(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buyerDB = mDatabase.child("Buyer");
        userDB = buyerDB.child(username);
        cartDB = userDB.child("Cart");
        myCart = new ArrayList<Product>();
        cartDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    deleteFromFirebase(String.valueOf(postSnapshot.getKey()));
                    myCart.clear();
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    public void placeOrder(View view){
        //Toast.makeText(this, "Your order has been placed", Toast.LENGTH_SHORT).show();
        //Send to owner
        for(Product order: myCart){
            String ownerName = order.getOwner();
            sellerDB = mDatabase.child("Seller");
            ownerDB = sellerDB.child(ownerName);
            //Toast.makeText(this, "username in place order" + username, Toast.LENGTH_SHORT).show();
            //get references to the cart of the buyer and pantry of seller so they can be modified
            pantryDB = ownerDB.child("Inventory");
            productDB = pantryDB.child(order.getProductName());
            final int quantityRequested = order.getQuantity();
            productName = order.getProductName();
            /*productDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //String chilCount = String.valueOf(postSnapshot.getChildrenCount());
                    //Toast.makeText(CartNavActivity.this, "Children Count" + chilCount, Toast.LENGTH_SHORT).show();
                    //for (DataSnapshot dataSnapshot: postSnapshot.getChildren()) {
                       // quantityString = dataSnapshot.child("Inventory").child(productName).child("Quantity").getValue().toString();
                        quantityString = dataSnapshot.child("Quantity").getValue().toString();
                        int quantityInt = Integer.valueOf(quantityString);
                        productDB.child("Quantity").setValue(quantityInt - quantityRequested);
                        //Toast.makeText(CartNavActivity.this, "inside ondatchanged" + quantityString, Toast.LENGTH_SHORT).show();
                    //}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            */



            //int quantityInt = Integer.valueOf(quantityString);
            //Toast.makeText(this, "quantity String" + quantityString, Toast.LENGTH_SHORT).show();
            //productDB.child("Quantity").setValue( quantityInt- quantityRequested);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Order").child(ownerName).child(username).child(order.getProductName()).child("Category").setValue(order.getProductCategory()); //username = username of the person requesting the order
            mDatabase.child("Order").child(ownerName).child(username).child(order.getProductName()).child("Price").setValue(order.getPrice());
            mDatabase.child("Order").child(ownerName).child(username).child(order.getProductName()).child("Quantity").setValue(order.getQuantity());
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        buyerDB = mDatabase.child("Buyer");
        userDB = buyerDB.child(username);
        cartDB = userDB.child("Cart");
        myCart = new ArrayList<Product>();
        cartDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    deleteFromFirebase(String.valueOf(postSnapshot.getKey()));
                    myCart.clear();
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    public void ReadData(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buyerDB = mDatabase.child("Buyer");
        userDB = buyerDB.child(username);
        cartDB = userDB.child("Cart");
        myCart = new ArrayList<Product>();
        cartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                myCart.clear();
                //DataSnapshot productNames = snapshot.child("Name");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String productName = String.valueOf(postSnapshot.getKey());
                    String productPrice = String.valueOf(postSnapshot.child("Price").getValue());
                    String category = String.valueOf(postSnapshot.child("Category").getValue());
                    String theQuantity = String.valueOf(postSnapshot.child("Quantity").getValue());
                    String ownerStr = String.valueOf(postSnapshot.child("Owner").getValue());
                    ownertoReceive = ownerStr;

                    try {
                        String priceReformat = String.format("%.2f", Double.parseDouble(productPrice));

                        Product newProduct = new Product(category, Double.parseDouble(priceReformat), productName, Integer.parseInt(theQuantity), ownerStr);
                        myCart.add(newProduct);
                    }
                    catch(NumberFormatException e){}
                }

                //For every product in myCart, add up the price
                Double totalCostInt = 0.00;
                for(Product item: myCart){
                    Double price = item.getPrice();
                    int amount = item.getQuantity();
                    totalCostInt = totalCostInt + (price*amount);
                }
                String totalReformat = String.format("%.2f", totalCostInt);
                totalCost.setText("$" + totalReformat);

                adapter = new listAdapterProducts(CartNavActivity.this, myCart);
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }


    public void deleteFromFirebase(String productName){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        cartDB = mDatabase.child("Buyer").child(username).child("Cart");
        //myCart = new ArrayList<Product>();
        cartDB.child(productName).child("Category").setValue(null);
        cartDB.child(productName).child("Owner").setValue(null);
        cartDB.child(productName).child("Price").setValue(null);
        cartDB.child(productName).child("Quantity").setValue(null);
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
        getMenuInflater().inflate(R.menu.cart_nav, menu);
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

        if (id == R.id.nav_cart) {
            // Handle the camera action
            Intent intent = new Intent(this, CartNavActivity.class);
            //intent.putExtra("MY_USERNAME", currentUser);//pass name of current user
            intent.putExtra("MY_USERNAME", username);//pass name of current user
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, BuyerProfileActivity.class);
            intent.putExtra("MY_USERNAME", username);
            startActivity(intent);

        } else if (id == R.id.nav_Search) {
            Intent intent = new Intent(this, ProductSearchActivity.class);
            intent.putExtra("MY_USERNAME", username);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
