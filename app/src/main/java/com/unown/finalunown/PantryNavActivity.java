package com.unown.finalunown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class PantryNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private static final String savedThingsKey = "savedThings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Farm Table: Pantry");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        list = (ListView) findViewById(R.id.listPantryNav);
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
            //Toast.makeText(this.getApplicationContext(), "in the first if statemnet", Toast.LENGTH_SHORT).show();
            myPantry = myContent.readDB(db, cursor, myPantry, username);
            myContent.updateDB(db, myDbHelper, myPantry, "pantry");
        } else if (cursor.moveToFirst()) {
            myPantry = myContent.readDB(db,cursor,myPantry, username);
            //myContent.updateDB(db, myDbHelper, myPantry, "pantry");
        } else {
            myPantry = myContent.populateArray(myPantry, username);
            myContent.insertThings(db, myPantry);
            myPantry = myContent.readDB(db, cursor, myPantry, username);
        }

        //readData();
        adapter = new listAdapterProducts(PantryNavActivity.this, myPantry);
        list.setAdapter(adapter);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSQLiteData();
            }


        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

                adapter = new listAdapterProducts(PantryNavActivity.this, myPantry);
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
                if (!myPantry.contains(newProduct)) {
                    myPantry.add(newProduct);

                    /*//add new item to firebase
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
*/
                    //for (int i = 0; i < myPantry.size(); i++) {
                        //Toast.makeText(this.getApplicationContext(), myPantry.get(i).getProductName(), Toast.LENGTH_SHORT).show();
                    //}
                }
                myContent.updateDB(db, myDbHelper, myPantry, "pantry");

                adapter.notifyDataSetChanged();

            }
        }
    }


    public void addItemToPantry(View view){
        Intent intent = new Intent(this, EditPantryActivity.class);
        startActivityForResult(intent, 1);
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
        getMenuInflater().inflate(R.menu.pantry_nav, menu);
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
            intent.putExtra("MY_USERNAME", username);//pass name of current user
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, SellerProfileActivity.class);
            intent.putExtra("MY_USERNAME", username);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(this, OrderReqNavActivity.class);
            intent.putExtra("MY_USERNAME", username);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
