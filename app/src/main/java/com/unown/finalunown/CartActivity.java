package com.unown.finalunown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    //private ArrayList<Product> cartItems;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = (ListView) findViewById(R.id.listView);

        //Delete later -- prepopulating to test
        List<Product> cartItems = new ArrayList<Product>();
        Product p1 = new Product("Fruit", 2.00, "Cherries", 4);
        Product p2 = new Product("Vegetable", 3.00, "Carrots", 7);
        Product p3 = new Product("Vegetable", 4.00, "Tomatoes", 20);
        cartItems.add(p1);
        cartItems.add(p2);
        cartItems.add(p3);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cartItems);
        //listView.setAdapter(arrayAdapter);
    }
}
