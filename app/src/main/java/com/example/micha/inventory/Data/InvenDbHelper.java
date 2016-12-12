package com.example.micha.inventory.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.micha.inventory.Data.InvenContract.InvenEntry;

/**
 * Created by micha on 12/5/2016.
 */

public class InvenDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "inventoryDb.db";

    private static final int DATABASE_VERSION = 1;

    public static final String LOG_TAG = InvenDbHelper.class.getSimpleName();


    public InvenDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    String CREATE_DB_TABLE = "CREATE TABLE " + InvenEntry.TABLE_NAME + " ("
            + InvenEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InvenEntry.NAME + " TEXT, "
            + InvenEntry.PRICE + " INTEGER, "
            + InvenEntry.TOTAL_ITEM_SALES + " INTEGER DEFAULT 0, "
            + InvenEntry.SUPPLY + " INTEGER DEFAULT 0, "
            + InvenEntry.SUPPLIER_INFO + " TEXT);";

        db.execSQL(CREATE_DB_TABLE);
    }
    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
