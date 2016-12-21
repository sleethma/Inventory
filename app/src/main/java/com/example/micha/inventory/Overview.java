package com.example.micha.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.micha.inventory.Data.InvenContract.InvenEntry;
import com.example.micha.inventory.Data.InvenDbHelper;

import java.util.List;

public class Overview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVEN_LOADER = 0;
    InvenDbHelper mDbHelper;
    Cursor cursor;
    TextView activityOverview;
    private InvenAdapter mCursorAdapter;
    //Custom global cursorloader instance
    private CursorLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Overview.this, InvenEditor.class);
                startActivity(intent);
            }
        });

        //kickoff loader
        getLoaderManager().initLoader(INVEN_LOADER, null, this);

        //create listview for activity
        ListView invenListView = (ListView) findViewById(R.id.list_view_inventory);

        invenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Overview.this, InvenEditor.class);
                //Form the content URI that represents the specific pet that was clicked on by
                //appending the 'id' (passed as input to this method) onto
                // the {@link PetEntry.CONTENT_URI}. For example, the content uri
                //content://android.example.pets/pets/2 if the pet with the id=2 was clicked on
                Uri currentInvenUri = ContentUris.withAppendedId(InvenEntry.CONTENT_URI, id);

                //set the uri on the data field of the intent to pass the uri with intent
                intent.setData(currentInvenUri);
                startActivity(intent);
            }
        });

        //Setup adapter to create a list item for each row of pet data in Cursor. @param null: null because
        //data not yet loaded into cursor.
        mCursorAdapter = new InvenAdapter(this, null);
        invenListView.setAdapter(mCursorAdapter);

        //set empty view
        View emptyview = findViewById(R.id.empty_view);
        invenListView.setEmptyView(emptyview);
    }

    private void createDb(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InvenEntry.NAME, "CPUs");
        values.put(InvenEntry.PRICE, 550);
        values.put(InvenEntry.SUPPLY, 2);
        values.put(InvenEntry.TOTAL_ITEM_SALES, 5);

        db.insert(InvenEntry.TABLE_NAME, null, values);
    }

    private Cursor queryDb(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InvenEntry._ID,
                InvenEntry.NAME,
                InvenEntry.PRICE,
                InvenEntry.SUPPLY,
                InvenEntry.TOTAL_ITEM_SALES};

        cursor = db.query(
                InvenEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }


    //edit and use for display listitem View in Cursor Adapter
    private void displayDbInfo(Cursor cursor){

        String name ="";
        int price;
        int supply;
        int itemsSold;

        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(InvenEntry.NAME);
        name = cursor.getString(nameColumnIndex);

        int priceColumnIndex = cursor.getColumnIndex(InvenEntry.PRICE);
        price = cursor.getInt(priceColumnIndex);

        int supplyColumnIndex = cursor.getColumnIndex(InvenEntry.SUPPLY);
        supply = cursor.getInt(supplyColumnIndex);

        int itemsSoldColumnIndex = cursor.getColumnIndex(InvenEntry.TOTAL_ITEM_SALES);
        itemsSold = cursor.getInt(itemsSoldColumnIndex);

        String dummyData = name + "\n" + price + "\n" + supply + "\n" + itemsSold;

        //activityOverview.setText(dummyData);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //make projection
        String[] projection = {InvenEntry._ID, InvenEntry.NAME, InvenEntry.SUPPLY, InvenEntry.PRICE,
                InvenEntry.TOTAL_ITEM_SALES, InvenEntry.SUPPLIER_INFO};


        //This loader will excecute the ContentProvider's query method on the background thread
        return new CursorLoader(this, InvenEntry.CONTENT_URI, projection, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
