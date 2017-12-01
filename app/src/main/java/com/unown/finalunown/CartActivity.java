package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    //private ArrayList<Product> cartItems;
    ListView list;
    Button placeOrderButton, clearCartButton;
    TextView totalCost;
    ArrayList<Product> myCart;
    String username;
    String ownertoReceive;
    listAdapterProducts adapter;
    private DatabaseReference mDatabase, buyerDB, userDB, cartDB, ownerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        adapter = new listAdapterProducts(CartActivity.this, myCart);
        list.setAdapter(adapter);


        //allow user to click on items to delete from cart
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){
                deleteFromFirebase(myCart.get(position).getProductName(), myCart.get(position).getOwner());
                //myCart.remove(position);
                // adapter.notifyDataSetChanged();
                //update cost!
            }
        });

    }

    public void clearMyCart(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buyerDB = mDatabase.child("Buyer");
        userDB = buyerDB.child(username);
        cartDB = userDB.child("Cart");
        myCart = new ArrayList<Product>();
        cartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    //Clear the caxrt/delete cart node and
                    postSnapshot.getRef().removeValue(); //delete cart
                    myCart.clear();
                }

                //listAdapterProductsSell adapter = new listAdapterProductsSell(CartActivity.this, myCart);
                //list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    public void placeOrder(View view){
        Toast.makeText(this, "Your order has been placed", Toast.LENGTH_SHORT).show();
        //Send to owner
        for(Product order: myCart){
            String ownerName = order.getOwner();
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
        cartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    //Clear the caxrt/delete cart node and
                    postSnapshot.getRef().removeValue(); //delete cart
                    myCart.clear();
                }

                //listAdapterProductsSell adapter = new listAdapterProductsSell(CartActivity.this, myCart);
                //list.setAdapter(adapter);
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
                //DataSnapshot productNames = snapshot.child("Name");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String productName = String.valueOf(postSnapshot.getKey());
                    String productPrice = String.valueOf(postSnapshot.child("Price").getValue());
                    String category = String.valueOf(postSnapshot.child("Category").getValue());
                    String theQuantity = String.valueOf(postSnapshot.child("Quantity").getValue());
                    String ownerStr = String.valueOf(postSnapshot.child("Owner").getValue());
                    ownertoReceive = ownerStr;

                    String priceReformat = String.format("%.2f", Double.parseDouble(productPrice));

                    Product newProduct = new Product(category, Double.parseDouble(priceReformat) , productName, Integer.parseInt(theQuantity), ownerStr);
                    myCart.add(newProduct);
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

                adapter = new listAdapterProducts(CartActivity.this, myCart);
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
        cartDB = mDatabase.child("Buyer").child(username).child("Cart");
        myCart = new ArrayList<Product>();
        cartDB.child(productName).setValue(null);

    }
}