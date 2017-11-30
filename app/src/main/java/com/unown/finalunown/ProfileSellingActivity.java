package com.unown.finalunown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileSellingActivity extends AppCompatActivity {

    TextView nameTextView;
    private DatabaseReference mDatabase, sellerDB, userDB, inventoryDB;
    ListView lv;
    ArrayList<Product> inventory;
    String nameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selling);



        lv = (ListView) findViewById(R.id.productListView);
        Intent passedIntent = getIntent();
        nameString =  passedIntent.getStringExtra("name");

        nameTextView = (TextView) findViewById(R.id.sellerNameTextViewSelling);

        nameTextView.setText(nameString);

        ReadData();

    }

    public void ReadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sellerDB = mDatabase.child("Seller");
        userDB = sellerDB.child(nameString);
        inventoryDB = userDB.child("Inventory");
        inventory = new ArrayList<Product>();

        inventoryDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                //Toast.makeText(ProfileSellingActivity.this, "count: " + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    //Toast.makeText(ProfileSellingActivity.this, "number of items: " + String.valueOf(postSnapshot.getValue()), Toast.LENGTH_SHORT).show();
                    String productName = String.valueOf(postSnapshot.child("name").getValue());
                    String productPrice = String.valueOf(postSnapshot.child("Price").getValue());
                    String category = String.valueOf(postSnapshot.child("Category").getValue());
                    Product newProduct = new Product(category, Double.valueOf(productPrice) , productName, 2, nameString);
                    inventory.add(newProduct);
                    //Toast.makeText(ProfileSellingActivity.this, "productName " + productName, Toast.LENGTH_LONG).show();
                    //Toast.makeText(ProfileSellingActivity.this, "inventory arraylist name" + inventory.get(0).getProductName(), Toast.LENGTH_SHORT).show();



                }

                listAdapterProductsSell adapter = new listAdapterProductsSell(ProfileSellingActivity.this, inventory, nameString);
                lv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

    }

}
