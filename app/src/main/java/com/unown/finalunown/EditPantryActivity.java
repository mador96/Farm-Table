package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPantryActivity extends AppCompatActivity {
    EditText nameET, categoryET, priceET, quantityET;
    Button addNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pantry);

        nameET = (EditText) findViewById(R.id.nameEditText);
        categoryET = (EditText) findViewById(R.id.categoryEditText);
        priceET = (EditText) findViewById(R.id.priceEditText);
        quantityET = (EditText) findViewById(R.id.quantityEditText);
        addNewItem = (Button) findViewById(R.id.addnewItem);

        addNewItem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String itemName = nameET.getText().toString();
                String itemCategory = categoryET.getText().toString();
                String itemPrice = priceET.getText().toString();
                String itemQuantity = quantityET.getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("ITEM_NAME", itemName);
                returnIntent.putExtra("ITEM_CATEGORY", itemCategory);
                returnIntent.putExtra("ITEM_PRICE", itemPrice);
                returnIntent.putExtra("ITEM_QUANTITY", itemQuantity);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });
    }

}
