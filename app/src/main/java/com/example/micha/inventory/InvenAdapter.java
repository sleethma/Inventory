package com.example.micha.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.micha.inventory.Data.InvenContract;

import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NAME;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.PRICE;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.SUPPLY;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.TOTAL_ITEM_SALES;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry._ID;
import static com.example.micha.inventory.InvenEditor.LOG_TAG;
import static java.security.AccessController.getContext;

/**
 * Created by micha on 12/17/2016.
 */

public class InvenAdapter extends CursorAdapter {
    Uri mCurrentUri;


    public InvenAdapter(Context context, Cursor c) {
        super(context, c, 0 /*flags*/);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }


    //bind views that have already been inflated with current cursor info for list_item
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final Button newSale = (Button) view.findViewById(R.id.newSaleBut);
        TextView nameView = (TextView) view.findViewById(R.id.productName);
        TextView priceView = (TextView) view.findViewById(R.id.productPrice);
        final TextView supplyView = (TextView) view.findViewById(R.id.supply);
        TextView soldView = (TextView) view.findViewById(R.id.unitsSold);

        //id data for item
        final int rowId = cursor.getInt(cursor.getColumnIndex(_ID));

        int supply = cursor.getInt(cursor.getColumnIndex(SUPPLY));
        String nameStr = cursor.getString(cursor.getColumnIndex(NAME));
        int price = cursor.getInt(cursor.getColumnIndex(PRICE));
        int sold = cursor.getInt(cursor.getColumnIndex(TOTAL_ITEM_SALES));

        supplyView.setText(String.valueOf(supply));
        nameView.setText(nameStr);
        priceView.setText(String.valueOf(price));
        soldView.setText(String.valueOf(sold));

        //hide Sell Button, if stock is zero
        if (supply == 0) {
            newSale.setVisibility(View.INVISIBLE);
        } else {
            newSale.setVisibility(View.VISIBLE);
        }

        //activates new button click in list_item_view
        newSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int supply = Integer.parseInt(supplyView.getText().toString());

                if (supply >= 1) {
                    supply -= 1;
                    supplyView.setText(String.valueOf(supply));

                    ContentValues values = new ContentValues();
                    values.put(InvenContract.InvenEntry.SUPPLY, supply);

                    //Create new uri with appended id
                    Uri currentUri = ContentUris.withAppendedId(
                            InvenContract.InvenEntry.CONTENT_URI, rowId);

                    int rowsAffected = context.getContentResolver().update(currentUri, values, null, null);

                    if (rowsAffected < 1) {
                        Log.e(LOG_TAG, "Supply not updated");
                    }

                } else {
                    newSale.setVisibility(View.GONE);
                }
            }

        });
    }
}

