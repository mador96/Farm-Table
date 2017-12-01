package com.unown.finalunown;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class EditPantryActivity extends AppCompatActivity {
    EditText nameET, categoryET, priceET, quantityET;
    Button addNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pantry);

        //toolbar?
        Toolbar editPantryToolbar = (Toolbar) findViewById(R.id.editToolbar);
        setSupportActionBar(editPantryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Pantry");
        //toolbar?

        nameET = (EditText) findViewById(R.id.nameEditText);
        categoryET = (EditText) findViewById(R.id.categoryEditText);
        priceET = (EditText) findViewById(R.id.priceEditText);
        quantityET = (EditText) findViewById(R.id.quantityEditText);
        addNewItem = (Button) findViewById(R.id.addnewItem);

        try {
            addNewItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String itemName = nameET.getText().toString();
                    String itemCategory = categoryET.getText().toString();
                    String itemPrice = priceET.getText().toString();
                    String itemQuantity = quantityET.getText().toString();
                    Double.parseDouble(itemPrice);
                    Integer.parseInt(itemQuantity);

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
        catch(NumberFormatException e){
            Toast.makeText(this, "Quantity and Price must be numeric inputs", Toast.LENGTH_SHORT).show();
        }
    }
    //If cancel button is pressed, go back to main activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}
