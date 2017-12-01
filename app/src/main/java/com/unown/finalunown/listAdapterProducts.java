package com.unown.finalunown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by ErinA on 11/13/2017.
 */
//to display a list of products
public class listAdapterProducts extends ArrayAdapter {


    //ChecklistContent checklistContent = new ChecklistContent(getContext());
    String name;
    String distance;
    Context context;
    ArrayList<Product> myList;
    ProductSearchActivity mySearchActivity;

    public listAdapterProducts(Context context, ArrayList<Product> resource){
        super(context,R.layout.list_view_row,resource);
        this.context=context;
        myList = resource;
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
        convertView = inflater.inflate(R.layout.list_view_row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.sellerNameTextView);
        //TextView distance = (TextView) convertView.findViewById(R.id.distanceTextView);
        TextView price = (TextView) convertView.findViewById(R.id.priceTextView);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantityTextView);
        TextView owner = (TextView) convertView.findViewById(R.id.ownerTextView);
        final String nameString = myList.get(position).getProductName();
        final double priceDouble = myList.get(position).getPrice();
        final int quantityInt = myList.get(position).getQuantity();
        final String ownerString = myList.get(position).getOwner();
        name.setText(nameString);
        String priceReformat = String.format("%.2f", priceDouble);
        price.setText("$" + priceReformat);
        quantity.setText("x" + Integer.toString(quantityInt));
        owner.setText("Owner: " + ownerString);

            /*
            double lat = myList.get(position).getLocationLatitude();
            double longit = myList.get(position).getLocationLongitude();
            String location = (lat +", " + longit);
            */

        /*
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileSellingActivity.class);
                intent.putExtra("name", nameString);
                intent.putExtra("price", priceDouble); //need this?
                intent.putExtra("quantity", quantityInt); //need this?
                intent.putExtra("quantity", ownerString);//need this?
                context.startActivity(intent);
            }
        });
        */
        return convertView;
    }
}
