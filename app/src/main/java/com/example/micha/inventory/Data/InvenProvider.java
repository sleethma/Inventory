package com.example.micha.inventory.Data;

import com.example.micha.inventory.Data.InvenContract.InvenEntry;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NAME;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.TABLE_NAME;

/**
 * Created by micha on 12/8/2016.
 */

public class InvenProvider extends ContentProvider {

    //return ID for Uri matches
    public static final int INVEN_ITEM_MATCH = 100;
    public static final int INVEN_TABLE_MATCH = 200;
    private final static String LOG_TAG = InvenProvider.class.getSimpleName();
    //UriMatcher global variable
    private static UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //set matches with ContentAuthority, path, and access id
    static {
        sUrimatcher.addURI(
                InvenContract.CONTENT_AUTHORITY, InvenContract.PATH_INVEN + "/#", INVEN_ITEM_MATCH);
        sUrimatcher.addURI(
                InvenContract.CONTENT_AUTHORITY, InvenContract.PATH_INVEN, INVEN_TABLE_MATCH);
    }

    //instantiate dbHelper
    private InvenDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InvenDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //get readable db
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;


        //see if UriMatcher can match URI to match code
        int match = sUrimatcher.match(uri);

        switch (match){
            case INVEN_TABLE_MATCH:

                cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
                break;
            case INVEN_ITEM_MATCH:
                // For the INVEN_ITEM_MATCH code, extract out the ID from the URI.
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of this case.
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.

                selection = InvenEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = db.query(TABLE_NAME,
                        projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //set notification URI on the cursor
        //so we know what content uri the cursor was created for
        //If the data at this uri is changed, then we know we need to update the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int match = sUrimatcher.match(uri);

        switch (match){
            case INVEN_TABLE_MATCH:
                return insertInven(uri, values);
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI " + uri);
        }
    }

    private Uri insertInven(Uri uri, ContentValues values){
        //perform data checks for invalid data
        String name = values.getAsString(NAME);
        if (name.isEmpty()) {
            Log.e(LOG_TAG, "name field cannot be left blank");
            Toast.makeText(getContext(), "Please enter product name", Toast.LENGTH_SHORT).show();
            return null;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if(id == -1){
            Log.e(LOG_TAG, "Error Inserting Row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
