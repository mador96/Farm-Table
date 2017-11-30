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
//used to display a list of seller objects

public class listAdapter extends ArrayAdapter {


        //ChecklistContent checklistContent = new ChecklistContent(getContext());
        String name;
        String distance;
        Context context;
        ArrayList<Seller> myList;
        ProductSearchActivity mySearchActivity;

        public listAdapter(Context context, ArrayList<Seller> resource){
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
            final String nameString = myList.get(position).getName();
            name.setText(nameString);

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
                    context.startActivity(intent);

                }
            });
                */
            return convertView;
        }
}


