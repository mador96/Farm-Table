package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantryActivity extends AppCompatActivity {

    ListView list;
    FloatingActionButton addItem;
    ArrayList<Product> myPantry;
    String username;
    listAdapterProducts adapter;
    private DatabaseReference mDatabase, sellerDB, userDB, pantryDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        list = (ListView) findViewById(android.R.id.list);

        addItem = (FloatingActionButton) findViewById(R.id.addItem);


        Intent passedIntent = getIntent();
        username = passedIntent.getStringExtra("MY_USERNAME");
        readData();
        adapter = new listAdapterProducts(PantryActivity.this, myPantry);
        list.setAdapter(adapter);
    }

    public void readData(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        userDB = sellerDB.child(username);
        pantryDB = userDB.child("Inventory");
        myPantry = new ArrayList<Product>();

        pantryDB.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot){
                String theQuantity = "";
                myPantry.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    String productName = String.valueOf(postSnapshot.getKey());
                    String productPrice = String.valueOf(postSnapshot.child("Price").getValue());
                    String category = String.valueOf(postSnapshot.child("Category").getValue());
                    theQuantity = String.valueOf(postSnapshot.child("Quantity").getValue());
                    String owner = String. valueOf(postSnapshot.child("Owner").getValue());

                    try {
                        Product newProduct = new Product(category, Double.parseDouble(productPrice), productName, Integer.parseInt(theQuantity), owner);
                        myPantry.add(newProduct);
                    }
                    catch(NumberFormatException e){
                        //Just using this to catch null error
                    }

                }

                adapter = new listAdapterProducts(PantryActivity.this, myPantry);
                list.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError firebaseError){
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String itemNameStr = data.getStringExtra("ITEM_NAME");
                String itemCategoryStr = data.getStringExtra("ITEM_CATEGORY");
                String itemPriceStr = data.getStringExtra("ITEM_PRICE");
                String itemQuantityStr = data.getStringExtra("ITEM_QUANTITY");
                //String itemOwnerStr = data.getStringExtra("ITEM_OWNER");
                double itemPrice = Double.parseDouble(itemPriceStr);
                int itemQuantity = Integer.parseInt(itemQuantityStr);


                mDatabase = FirebaseDatabase.getInstance().getReference();
                sellerDB = mDatabase.child("Seller");
                userDB = sellerDB.child(username);
                pantryDB = userDB.child("Inventory");
                myPantry = new ArrayList<Product>();

                mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr);
                mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Price").setValue(itemPrice);
                mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Quantity").setValue(itemQuantity);
                mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Category").setValue(itemCategoryStr);
                mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Category").setValue(username);

                Product newProduct = new Product(itemCategoryStr, itemPrice, itemNameStr, itemQuantity, username);
                myPantry.add(newProduct);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void addItemToPantry(View view){
        Intent intent = new Intent(this, EditPantryActivity.class);
        startActivityForResult(intent, 1);
    }


}
