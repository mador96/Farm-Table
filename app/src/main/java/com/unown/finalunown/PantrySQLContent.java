package com.unown.finalunown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PantrySQLContent {
    //public static ArrayList<ExerciseItem> myList = new ArrayList<>();


    private static Context context;

    public PantrySQLContent(Context c) {
        context = c;
    }

    //public void setList(ArrayList<ExerciseItem> exerciseList) {
    //    myList = exerciseList;
    //}
    public ArrayList<Product> readDB(SQLiteDatabase db, Cursor cursor, ArrayList<Product> pantryList, String username) {
        if (cursor.moveToFirst()) { // add all of the items from the DB to the list
            String category = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_CATEGORY));
            String price = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_PRICE));
            String productName = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_PRODUCT));
            String  quantity = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_QUANTITY));
            String owner = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_OWNER));
            Product myProduct = new Product(category, Double.valueOf(price), productName, Integer.valueOf(quantity), owner);
            if (username.equals(owner)) {
                if (pantryList.contains(myProduct)) {  //if the exercise is in the db already
                    pantryList.remove(myProduct);      //make sure it is an update version
                    pantryList.add(myProduct);
                } else {
                    pantryList.add(myProduct);
                }  ////else just add it if it's new
            }
            while (cursor.moveToNext()) {
                category = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_CATEGORY));
                price = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_PRICE));
                productName = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_PRODUCT));
                quantity = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_QUANTITY));
                owner = cursor.getString(cursor.getColumnIndex(DbHelper.FeedEntry.COLUMN_NAME_OWNER));
                myProduct = new Product(category, Double.valueOf(price), productName, Integer.valueOf(quantity), owner);
                if (username.equals(owner)) { //check to see if this product belongs to the user looking at their pantry
                    if (pantryList.contains(myProduct)) { //if the exercise is in the db already
                        pantryList.remove(myProduct);     //make sure it is an update version
                        pantryList.add(myProduct);
                    } else {
                        pantryList.add(myProduct);  //else just add it if it's new
                    }
                }
            }
        }
        return pantryList;
    }


    public void updateDB(SQLiteDatabase db, DbHelper myDbHelper, ArrayList<Product> pantryList, String tableName) {
        myDbHelper.onDelete(db); //delete old db
        myDbHelper.onCreate(db); //create new db (this probs isn't efficient but was the best option for my last
        //interaction with a DB so I should change
        for (int i = 0; i < pantryList.size(); i++) {
            ContentValues pantryValues = new ContentValues();
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_CATEGORY, pantryList.get(i).getProductCategory());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_PRICE, pantryList.get(i).getPrice());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_PRODUCT, pantryList.get(i).getProductName());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_QUANTITY, pantryList.get(i).getQuantity());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_OWNER, pantryList.get(i).getOwner());
            db.insert(tableName, null, pantryValues); //insert new row
        }
    }


    //puts things into the database from the checklistModelItems array
    public void insertThings(SQLiteDatabase db, ArrayList<Product> pantryList) {
        for (int i = 0; i < pantryList.size(); i++) {
            ContentValues pantryValues = new ContentValues();
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_CATEGORY, pantryList.get(i).getProductCategory());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_PRICE, pantryList.get(i).getPrice());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_PRODUCT, pantryList.get(i).getProductName());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_QUANTITY, pantryList.get(i).getQuantity());
            pantryValues.put(DbHelper.FeedEntry.COLUMN_NAME_OWNER, pantryList.get(i).getOwner());
            db.insert(DbHelper.FeedEntry.TABLE_NAME, null, pantryValues); //insert new row
        }

    }

    public void addListItem(ArrayList<Product> exerciseItemArrayList, String category,String price, String productName, String quantity, String owner) {
        Product myItem = new Product(category, Double.valueOf(price), productName, Integer.valueOf(quantity), owner);
        if (!exerciseItemArrayList.contains(myItem)) {
            exerciseItemArrayList.add(myItem);
        } else {
            Toast.makeText(context, "This exercise already exists", Toast.LENGTH_SHORT).show();
        }
    }


    //fills checklistModelItems with the list of things to do
    public ArrayList<Product> populateArray(ArrayList<Product> pantryList, String owner) {
        Product thing1 = new Product("fruit", 0.30, "apple", 1, owner);
        Product thing2 = new Product("vegetable", 0.45, "carrot", 1, owner);
        pantryList.add(thing1);
        pantryList.add(thing2);

        return pantryList;

    }


}



