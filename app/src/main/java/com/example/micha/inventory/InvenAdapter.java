package com.example.micha.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NAME;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.PRICE;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.SUPPLY;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.TOTAL_ITEM_SALES;
import static java.security.AccessController.getContext;

/**
 * Created by micha on 12/17/2016.
 */

public class InvenAdapter extends CursorAdapter {

    public int mSupply = 0;
    TextView supplyView;

    public InvenAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }


    //bind views that have already been inflated with current cursor info for list_item
    @Override
    public void bindView(final View view, Context context, Cursor cursor) {

        mSupply = 0;

        Button newSale = (Button) view.findViewById(R.id.newSaleBut);
        TextView nameView = (TextView) view.findViewById(R.id.productName);
        TextView priceView = (TextView) view.findViewById(R.id.productPrice);
        supplyView = (TextView) view.findViewById(R.id.supply);
        TextView soldView = (TextView) view.findViewById(R.id.unitsSold);

        int supply = cursor.getInt(cursor.getColumnIndex(SUPPLY));
        String nameStr = cursor.getString(cursor.getColumnIndex(NAME));
        int price = cursor.getInt(cursor.getColumnIndex(PRICE));
        int sold = cursor.getInt(cursor.getColumnIndex(TOTAL_ITEM_SALES));
        mSupply = supply;

        //activates new button click in list_item_view
        newSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSupply += 1;
                refreshSupply();
            }
        });

        supplyView.setText(String.valueOf(mSupply));
        nameView.setText(nameStr);
        priceView.setText(String.valueOf(price));
        soldView.setText(String.valueOf(sold));

        //reset mSupply for next view
        //mSupply = 0;
    }

    private void refreshSupply() {
        supplyView.setText(String.valueOf(mSupply));
    }
}

