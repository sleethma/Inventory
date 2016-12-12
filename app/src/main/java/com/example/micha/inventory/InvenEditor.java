package com.example.micha.inventory;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.micha.inventory.Data.InvenContract.InvenEntry;

import com.example.micha.inventory.Data.InvenContract;

/**
 * Created by micha on 12/10/2016.
 */

public class InvenEditor extends AppCompatActivity {

    //Initialize Views
    EditText mProductName;
    EditText mProductPrice;
    EditText mUnitsSold;
    EditText mInStock;
    EditText mNumToShip;
    EditText mNewSold;
    EditText mSupplierInfo;
    EditText mNumToOrder;
    Button mDeleteProduct;
    ImageButton mSaveProduct;

    Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_detail_view);

        mProductName = (EditText)  findViewById(R.id.productName);
        mProductPrice = (EditText)  findViewById(R.id.productPrice);
        mUnitsSold = (EditText)  findViewById(R.id.unitsSold);
        mInStock = (EditText)  findViewById(R.id.inStock);
        mNumToShip = (EditText)  findViewById(R.id.numToShip);
        mNewSold = (EditText)  findViewById(R.id.newSold);
        mSupplierInfo = (EditText)  findViewById(R.id.supplierInfo);
        mNumToOrder = (EditText)  findViewById(R.id.numToOrder);
        mDeleteProduct = (Button) findViewById(R.id.deleteProduct);
        mSaveProduct = (ImageButton) findViewById(R.id.saveProduct);



        mDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInven();
            }
        });

    }

    private void saveInven(){
        //get info from EditText views
        String name = mProductName.getText().toString().trim();
        String priceString = mProductPrice.getText().toString().trim();
        String soldString = mUnitsSold.getText().toString().trim();
        String inStockString = mInStock.getText().toString().trim();
        String supplierInfo = mSupplierInfo.getText().toString().trim();


        //Setup ContentValues
        ContentValues values = new ContentValues();
        values.put(InvenEntry.NAME, name);
        values.put(InvenEntry.SUPPLIER_INFO, supplierInfo);

        //set all int vars to 0
        int price = 0;
        int sold = 0;
        int inStock = 0;

        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        if (!TextUtils.isEmpty(soldString)) {
            sold = Integer.parseInt(soldString);
        }
        if (!TextUtils.isEmpty(inStockString)) {
            inStock = Integer.parseInt(inStockString);
        }
        values.put(InvenEntry.PRICE, price);
        values.put(InvenEntry.TOTAL_ITEM_SALES, sold);
        values.put(InvenEntry.SUPPLY, inStock);



        //check to see if new product or not
        if (mCurrentUri == null){
            Uri newUri = getContentResolver().insert(InvenEntry.CONTENT_URI, values);

            if (newUri ==null){
                Toast.makeText(this, "Error Saving Product",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Product Saved Successfully",Toast.LENGTH_SHORT).show();
            }
        }else{
            //This is an existing entry so update db with existing mCurrentUri
            int rowsUpdated = getContentResolver().update(mCurrentUri,values,null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsUpdated == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this,"Update Unsuccessful",Toast.LENGTH_LONG).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this,"Update Successful!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
