package com.unown.finalunown;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ErinA on 6/1/2017.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "pantry";
        public static final String COLUMN_NAME_PRODUCT = "productName";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_OWNER = "owner";
    }


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "farmTable.db";
    private static DbHelper sInstance;

    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " +
            DbHelper.FeedEntry.TABLE_NAME + " (" + DbHelper.FeedEntry._ID + " INTEGER PRIMARY KEY,"
            + FeedEntry.COLUMN_NAME_PRODUCT + " TEXT, " + FeedEntry.COLUMN_NAME_PRICE + " TEXT, "
            + FeedEntry.COLUMN_NAME_CATEGORY +  " TEXT, " +
            FeedEntry.COLUMN_NAME_QUANTITY + " TEXT, "+DbHelper.FeedEntry.COLUMN_NAME_OWNER + " TEXT)";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME );
    }

    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME );
        onCreate(db);
    }

    public static synchronized DbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }







}

