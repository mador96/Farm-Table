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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by ErinA on 11/13/2017.
 */
//to display a list of products
public class listAdapterProductsSell extends ArrayAdapter {


    //ChecklistContent checklistContent = new ChecklistContent(getContext());
    String name;
    String distance;
    Context context;
    ArrayList<Product> myList;
    ProductSearchActivity mySearchActivity;
    TextView nameTextView;
    Button addProduct;
    String username;
    String quanInt;
    private DatabaseReference mDatabase,sellerDB, buyerDB, sellerNameDB, buyerNameDB, pantryDB, cartDB, productDB;
    private DatabaseReference orderDB, orderSellerDB, orderNameDB;

    public listAdapterProductsSell(Context context, ArrayList<Product> resource, String passedUsername){
        super(context,R.layout.sell_product_list_view_row,resource);
        this.context=context;
        myList = resource;
        username = "kyle";

        //checklistContent.showList=resource;
        //itemChecked1 = new ArrayList<Boolean>();
           /* for (int i = 0; i < checklistContent.showList.size(); i++) {
                itemChecked1.add(i,checklistContent.showList.get(i).getValue()==0);
            }
            */

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.sell_product_list_view_row, parent, false);
        nameTextView = (TextView) convertView.findViewById(R.id.item_name);
        addProduct = (Button) convertView.findViewById(R.id.add_prod_button);
        //TextView distance = (TextView) convertView.findViewById(R.id.distanceTextView);
        //TextView price = (TextView) convertView.findViewById(R.id.priceTextView);
        //TextView quantity = (TextView) convertView.findViewById(R.id.quantityTextView);
        final String nameString = myList.get(position).getProductName();
        final double priceDouble = myList.get(position).getPrice();
        final int quantityInt = myList.get(position).getQuantity();
        final String ownerString = myList.get(position).getOwner();
        final String categoryString = myList.get(position).getProductCategory();
        nameTextView.setText(nameString);

        //price.setText("$" + Double.toString(priceDouble));
        //quantity.setText("x" + Integer.toString(quantityInt));

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
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                 //amountWanted = userInputDialogEditText.getText().toString();

                                // ToDo get user input here
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



                //add item to the buyers cart
                mDatabase = FirebaseDatabase.getInstance().getReference();
                //get references to the cart of the buyer and pantry of seller so they can be modified
                sellerDB = mDatabase.child("Seller");
                sellerNameDB = sellerDB.child(ownerString);
                pantryDB = sellerNameDB.child("Inventory");
                productDB = pantryDB.child(nameString);
                productDB.child("Quantity").setValue(33 - 1);


                orderDB = mDatabase.child("Order");
                orderSellerDB = orderDB.child(ownerString);
                orderNameDB = orderSellerDB.child(username);
                orderNameDB.child(nameString).child("Price").setValue(priceDouble);
                orderNameDB.child(nameString).child("Quantity").setValue(quantityInt);
                orderNameDB.child(nameString).child("Category").setValue(categoryString);
                orderNameDB.child(nameString).child("Owner").setValue(ownerString);


                buyerDB = mDatabase.child("Buyer");
                buyerNameDB = buyerDB.child(username);
                cartDB = buyerNameDB.child("Cart");

                //myPantry = new ArrayList<Product>();


                //TODO: give the option for how many they would like to add to their cart based on how many there are available
                cartDB.child(nameString).child("Price").setValue(priceDouble);
                cartDB.child(nameString).child("Quantity").setValue(quantityInt);
                cartDB.child(nameString).child("Category").setValue(categoryString);
                cartDB.child(nameString).child("Owner").setValue(ownerString);
                //TODO: add a dialog box for users to confirm that they would like to add the item to their cart,
                //TODO: then give them a toast that tells them the item was added to their cart

                //Product newProduct = new Product(itemCategoryStr, itemPrice, itemNameStr, itemQuantity, username);
                //myPantry.add(newProduct);
            }
        });

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileSellingActivity.class);
                intent.putExtra("name", nameString);
                intent.putExtra("price", priceDouble); //need this?
                intent.putExtra("quantity", quantityInt); //need this?
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}


