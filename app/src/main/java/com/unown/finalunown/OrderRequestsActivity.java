package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Sellers can see orders that are placed
public class OrderRequestsActivity extends AppCompatActivity {
    ListView list;
    String ownerUsername;
    ArrayList<Product> myOrders;
    listAdapterProducts adapter;

    private DatabaseReference mDatabase, buyerDB, userDB, cartDB, ownerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_requests);

        list = (ListView) findViewById(android.R.id.list);

        Intent passedIntent = getIntent();
        ownerUsername = passedIntent.getStringExtra("MY_USERNAME");

        readData();

        adapter = new listAdapterProducts(OrderRequestsActivity.this, myOrders);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){
                myOrders.remove(position);
                //test
                adapter.notifyDataSetChanged();

                /*
                mDatabase = FirebaseDatabase.getInstance().getReference();
                ownerDB = mDatabase.child("Order").child(ownerUsername);
                myOrders = new ArrayList<Product>();
                ownerDB.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        //myOrders.clear();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            //this gets buyer's username
                            for(DataSnapshot ps: postSnapshot.getChildren()){
                                for(DataSnapshot ppp: ps.getChildren()){
                                    ppp.getRef().removeValue();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.e("The read failed: " ,firebaseError.getMessage());
                    }
                });
                */
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
                adapter = new listAdapterProducts(OrderRequestsActivity.this, myOrders);
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }
}
