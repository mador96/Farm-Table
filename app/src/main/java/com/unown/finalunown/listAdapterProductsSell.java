package com.unown.finalunown;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by ErinA on 11/13/2017.
 */
//to display a list of products
public class listAdapterProductsSell extends ArrayAdapter implements Filterable {


    //ChecklistContent checklistContent = new ChecklistContent(getContext());
    String name;
    String distance;
    Context context;
    ArrayList<Product> myList;
    ProductSearchActivity mySearchActivity;
    TextView nameTextView, priceTextView, sellerTextView;
    Button addProduct;
    String username;
    String quanInt;
    private DatabaseReference mDatabase,sellerDB, buyerDB, sellerNameDB, buyerNameDB, pantryDB, cartDB, productDB;
    private DatabaseReference orderDB, orderSellerDB, orderNameDB;

    public listAdapterProductsSell(Context context, ArrayList<Product> resource, String passedUsername){
        super(context,R.layout.sell_product_list_view_row,resource);
        this.context=context;
        myList = resource;
        username = passedUsername;

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mySearchActivity.listOfProducts.clear();
        if (charText.length() == 0) {
            mySearchActivity.listOfProducts.addAll(myList);
        } else {
            for (Product wp : myList) {
                if (wp.getProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mySearchActivity.listOfProducts.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.sell_product_list_view_row, parent, false);
        nameTextView = (TextView) convertView.findViewById(R.id.item_name);
        sellerTextView = (TextView) convertView.findViewById(R.id.item_seller);
        priceTextView = (TextView) convertView.findViewById(R.id.item_price);
        addProduct = (Button) convertView.findViewById(R.id.add_prod_button);

        final String nameString = myList.get(position).getProductName();
        final double priceDouble = myList.get(position).getPrice();
        final int quantityInt = myList.get(position).getQuantity();
        final String ownerString = myList.get(position).getOwner();
        final String categoryString = myList.get(position).getProductCategory();
        nameTextView.setText(nameString);
        sellerTextView.setText(ownerString);
        String priceReformat = String.format("%.2f", priceDouble);
        priceTextView.setText("$" + priceReformat);
            /*
            double lat = myList.get(position).getLocationLatitude();
            double longit = myList.get(position).getLocationLongitude();

            String location = (lat +", " + longit);

            */

        addProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                userInputDialogEditText.setHint("1-"+quantityInt);

                //String amountWanted;
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String amountWanted = userInputDialogEditText.getText().toString();
                                try {
                                    int quantityRequested = Integer.valueOf(amountWanted);

                                    if (quantityRequested <= 0 || quantityRequested > quantityInt) {
                                        Toast.makeText(context, "Must request between 1 and " + quantityInt + " items"
                                                , Toast.LENGTH_SHORT).show();
                                        dialogBox.cancel();
                                    } else {
                                        mDatabase = FirebaseDatabase.getInstance().getReference();
                                        //get references to the cart of the buyer and pantry of seller so they can be modified
                                /*sellerDB = mDatabase.child("Seller");
                                sellerNameDB = sellerDB.child(ownerString);
                                pantryDB = sellerNameDB.child("Inventory");
                                productDB = pantryDB.child(nameString);
                                productDB.child("Quantity").setValue( quantityInt- quantityRequested);
                                */

                                        buyerDB = mDatabase.child("Buyer");
                                        buyerNameDB = buyerDB.child(username);
                                        cartDB = buyerNameDB.child("Cart");

                                        //myPantry = new ArrayList<Product>();


                                        //TODO: give the option for how many they would like to add to their cart based on how many there are available
                                        cartDB.child(nameString).child("Price").setValue(priceDouble);
                                        cartDB.child(nameString).child("Quantity").setValue(quantityRequested);
                                        cartDB.child(nameString).child("Category").setValue(categoryString);
                                        cartDB.child(nameString).child("Owner").setValue(ownerString);

                                    }
                                }
                                catch(NumberFormatException e){
                                    Toast.makeText(context, "Input must be a number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();




                //myPantry = new ArrayList<Product>();
/*

                //TODO: give the option for how many they would like to add to their cart based on how many there are available
                cartDB.child(nameString).child("Price").setValue(priceDouble);
                cartDB.child(nameString).child("Quantity").setValue(quantityInt);
                cartDB.child(nameString).child("Category").setValue(categoryString);
                cartDB.child(nameString).child("Owner").setValue(ownerString);
                //TODO: add a dialog box for users to confirm that they would like to add the item to their cart,
                //TODO: then give them a toast that tells them the item was added to their cart

                //Product newProduct = new Product(itemCategoryStr, itemPrice, itemNameStr, itemQuantity, username);
                //myPantry.add(newProduct);
                */
            }
        });

        /*nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileSellingActivity.class);
                intent.putExtra("name", nameString);
                intent.putExtra("price", priceDouble); //need this?
                intent.putExtra("quantity", quantityInt); //need this?
                context.startActivity(intent);
            }
        });
        */
        return convertView;
    }
}


