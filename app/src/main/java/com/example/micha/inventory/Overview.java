package com.example.micha.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.micha.inventory.Data.InvenContract.InvenEntry;
import com.example.micha.inventory.Data.InvenDbHelper;

public class Overview extends AppCompatActivity {

    InvenDbHelper mDbHelper;
    Cursor cursor;
    TextView activityOverview;

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
        mDbHelper = new InvenDbHelper(this);
        createDb();
        displayDbInfo(queryDb());
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
}
