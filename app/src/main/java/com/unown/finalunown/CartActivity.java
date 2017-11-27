package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    //private ArrayList<Product> cartItems;
    ListView list;
    Button placeOrderButton;
    TextView totalCost;
    ArrayList<Product> myCart;
    String username;
    private DatabaseReference mDatabase, buyerDB, userDB, cartDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        list = (ListView) findViewById(android.R.id.list);
        placeOrderButton = (Button) findViewById(R.id.placeOrderButton);
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



    }

    public void placeOrder(View view){
        Toast.makeText(this, "Your order has been placed", Toast.LENGTH_SHORT).show();
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
                    Product newProduct = new Product(category, Double.parseDouble(productPrice) , productName, Integer.parseInt(theQuantity));
                    myCart.add(newProduct);
                }

                //For every product in myCart, add up the price
                Double totalCostInt = 0.00;
                for(Product item: myCart){
                    Double price = item.getPrice();
                    int amount = item.getQuantity();
                    totalCostInt = totalCostInt + (price*amount);
                }
                //Set total cost for text view... two decimals???
                //DecimalFormat formatter = new DecimalFormat("####.00");
                //formatter.format(totalCostInt);
                totalCost.setText("$" + Double.toString(totalCostInt));

                listAdapterProducts adapter = new listAdapterProducts(CartActivity.this, myCart);
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }
}