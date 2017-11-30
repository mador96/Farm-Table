package com.unown.finalunown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.OkHttpClient;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

public class PantryActivity extends AppCompatActivity {

    ListView list;
    FloatingActionButton addItem;
    ArrayList<Product> myPantry;
    String username;
    listAdapterProducts adapter;
    Context context;
    private DatabaseReference mDatabase, sellerDB, userDB, pantryDB;
    PantrySQLContent myContent;
    Button upload;
    DbHelper myDbHelper;
    SQLiteDatabase db;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        //database visualizer on chrome://inspect
        Stetho.newInitializerBuilder(this).enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build();
        Stetho.initializeWithDefaults(this);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        list = (ListView) findViewById(android.R.id.list);
        addItem = (FloatingActionButton) findViewById(R.id.addItem);
        upload = (Button) findViewById(R.id.uploadButton);

        //initialize and create db
        myDbHelper = new DbHelper(this);
        db = myDbHelper.getWritableDatabase();
        cursor = db.rawQuery("select * from " + DbHelper.FeedEntry.TABLE_NAME, null);


        Intent passedIntent = getIntent();
        username = passedIntent.getStringExtra("MY_USERNAME");
        //TODO: fix the above make sure it gets the username of whoever is using the app
        myContent = new PantrySQLContent(context);
        myPantry = new ArrayList<>();

        if (savedInstanceState==null && cursor.moveToFirst()) {
            //Toast.makeText(this.getApplicationContext(), "in the first if stateemnet", Toast.LENGTH_SHORT).show();
            myPantry = myContent.readDB(db, cursor, myPantry, username);
            myContent.updateDB(db, myDbHelper, myPantry, "pantry");
        } else if (cursor.moveToFirst()) {
            myContent.updateDB(db, myDbHelper, myPantry, "pantry");
        } else {
            myPantry = myContent.populateArray(myPantry, username);
            myContent.insertThings(db, myPantry);
            myPantry = myContent.readDB(db, cursor, myPantry, username);
        }

        //readData();
        adapter = new listAdapterProducts(PantryActivity.this, myPantry);
        list.setAdapter(adapter);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSQLiteData();
            }

        });

    }

    public void uploadSQLiteData() {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            ///ArrayList<Product> pantryListSQL = new ArrayList();
            //pantryListSQL = myContent.readDB(db, cursor,pantryListSQL, username);
            DatabaseReference pantryDB, productDB, sellerDB, nameDB;
            sellerDB = mDatabase.child("Seller");
            nameDB = sellerDB.child(username);
            pantryDB = nameDB.child("Inventory");
            //pantryDB = mDatabase.child("Seller").child(username).child("Inventory");
            for (int i = 0; i < myPantry.size(); i++){
                String productName = myPantry.get(i).getProductName();
                Product curr = myPantry.get(i);
                productDB = pantryDB.child(productName);
                productDB.child("Category").setValue(curr.getProductCategory());
                productDB.child("Price").setValue(curr.getPrice());
                productDB.child("Owner").setValue(curr.getOwner());
                productDB.child("Quantity").setValue(curr.getQuantity());
            }
        Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
    }

    public void readData(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        //userDB = sellerDB.child(username);
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

        DbHelper myDbHelper = DbHelper.getInstance(this.getApplicationContext());
        final SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String itemNameStr = data.getStringExtra("ITEM_NAME");
                String itemCategoryStr = data.getStringExtra("ITEM_CATEGORY");
                String itemPriceStr = data.getStringExtra("ITEM_PRICE");
                String itemQuantityStr = data.getStringExtra("ITEM_QUANTITY");
                String itemOwnerStr = username;
                double itemPrice = Double.parseDouble(itemPriceStr);
                int itemQuantity = Integer.parseInt(itemQuantityStr);
                Product newProduct = new Product(itemCategoryStr, itemPrice, itemNameStr, itemQuantity, username);
                if (!myPantry.contains(newProduct)){
                    myPantry.add(newProduct);

                    //add new item to firebase
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    sellerDB = mDatabase.child("Seller");
                    userDB = sellerDB.child(username);
                    pantryDB = userDB.child("Inventory");
                    myPantry = new ArrayList<Product>();

                    mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr);
                    mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Price").setValue(itemPrice);
                    mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Quantity").setValue(itemQuantity);
                    mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Category").setValue(itemCategoryStr);
                    mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Owner").setValue(username);
                    //finished adding new item to firebase

                    for (int i = 0; i < myPantry.size(); i++){
                        //Toast.makeText(this.getApplicationContext(), myPantry.get(i).getProductName(), Toast.LENGTH_SHORT).show();
                    }
                }
                myContent.updateDB(db, myDbHelper, myPantry, "pantry");

                adapter.notifyDataSetChanged();

            }
        }
    }

    /*
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
                mDatabase.child("Seller").child(username).child("Inventory").child(itemNameStr).child("Owner").setValue(username);

                Product newProduct = new Product(itemCategoryStr, itemPrice, itemNameStr, itemQuantity, username);
                myPantry.add(newProduct);
                adapter.notifyDataSetChanged();
            }
        }
    }

*/


    public void addItemToPantry(View view){
        Intent intent = new Intent(this, EditPantryActivity.class);
        startActivityForResult(intent, 1);
    }


}
