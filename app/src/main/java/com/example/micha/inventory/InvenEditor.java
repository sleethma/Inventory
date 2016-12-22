package com.example.micha.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.inventory.Data.InvenContract.InvenEntry;


import static android.content.Intent.ACTION_SENDTO;
import static android.content.Intent.ACTION_SEND_MULTIPLE;
import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NAME;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NEW_SOLD;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NUM_TO_ORDER;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.NUM_TO_SHIP;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.PRICE;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.SUPPLIER_INFO;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.SUPPLY;
import static com.example.micha.inventory.Data.InvenContract.InvenEntry.TOTAL_ITEM_SALES;

/**
 * Created by micha on 12/10/2016.
 */

public class InvenEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = InvenEditor.class.getSimpleName();


    //CursorLoader id #
    private static final int URL_LOADER = 0;
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
    Button mProductImgBut;
    ImageButton mSaveProduct;
    ImageButton mOrderProduct;
    ImageView mProductImg;
    TextView mUploadInstruct;
    //Current Uri
    Uri mCurrentUri;
    /**
     * URI that will contain the path to the chosen image
     */
    private Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_detail_view);

        mProductName = (EditText) findViewById(R.id.productName);
        mProductPrice = (EditText) findViewById(R.id.productPrice);
        mUnitsSold = (EditText) findViewById(R.id.unitsSold);
        mInStock = (EditText) findViewById(R.id.inStock);
        mNumToShip = (EditText) findViewById(R.id.numToShip);
        mNewSold = (EditText) findViewById(R.id.newSold);
        mSupplierInfo = (EditText) findViewById(R.id.supplierInfo);
        mNumToOrder = (EditText) findViewById(R.id.numToOrder);
        mDeleteProduct = (Button) findViewById(R.id.deleteProduct);
        mSaveProduct = (ImageButton) findViewById(R.id.saveProduct);
        mOrderProduct = (ImageButton) findViewById(R.id.orderProduct);
        mProductImgBut = (Button) findViewById(R.id.productImgBut);
        mProductImg = (ImageView) findViewById(R.id.productImg);
        mUploadInstruct = (TextView) findViewById(R.id.uploadInstruct);

        //get event Intent from list_view
        Intent intent = getIntent();

        //examine the intent that was used to start this activity in order to find out if we are
        //starting the edit or the insert function of the activity, get data from intent
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle("Add Product");

        } else {
            setTitle("Edit Product");
            //initiate loader from manager
            getLoaderManager().initLoader(URL_LOADER, null, this);
        }

        mDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

        mSaveProduct.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInven();
            }
        });

        mOrderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });
        mProductImgBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), "Test Img click", Toast.LENGTH_LONG).show();
                uploadProductImg();
            }
        });
    }

    private void uploadProductImg() {
        Intent pictureIntent;

        if (Build.VERSION.SDK_INT < 19) {
            pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            pictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        pictureIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), 1);

    }

    /**
     * manage the returned path after selecting the picture
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // action cancelled
            Log.e(LOG_TAG, "onActivityResult() was canceled");
        }
        if (resultCode == RESULT_OK) {
            Uri selectedPicture = data.getData();
            pictureUri = selectedPicture;
            Log.v(LOG_TAG, "file path from OnActivityResult pictureUri: " + pictureUri);
            mProductImg.setImageURI(pictureUri);
            mUploadInstruct.setText("Edit Image");
        }
    }

    private void sendOrder() {
        if (Integer.parseInt(mNumToOrder.getText().toString()) > 0) {
            String orderAmt = mNumToOrder.getText().toString();
            String addresses = mSupplierInfo.getText().toString();
            String productName = mProductName.getText().toString();
            String message = "We would like to place an order for " + orderAmt + " more of product"
                    + " name: " + productName + ".";
            String[] addressArray = {addresses};
            String mailSubject = "Order request for " + productName;
            Intent emailIntent = new Intent(ACTION_SEND_MULTIPLE);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(EXTRA_EMAIL, addressArray);
            emailIntent.putExtra(EXTRA_SUBJECT, mailSubject);
            emailIntent.putExtra(EXTRA_TEXT, message);

            //says cannot find activity to accept intent
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Log.e(LOG_TAG, "Error with sending Intent");
            }
        } else {
            Toast.makeText(this, "Please enter order amount", Toast.LENGTH_LONG).show();
        }
    }

    private void saveInven() {
        //get info from EditText views
        String name = mProductName.getText().toString().trim();
        String priceString = mProductPrice.getText().toString().trim();
        String soldString = mUnitsSold.getText().toString().trim();
        String inStockString = mInStock.getText().toString().trim();
        String supplierInfo = mSupplierInfo.getText().toString().trim();
        String newSoldStr = mNewSold.getText().toString().trim();
        String numToShipStr = mNumToShip.getText().toString().trim();




        //Setup ContentValues
        ContentValues values = new ContentValues();
        values.put(InvenEntry.NAME, name);
        values.put(InvenEntry.SUPPLIER_INFO, supplierInfo);

        //set all int vars to 0
        int price = 0;
        int sold = 0;
        int inStock = 0;
        int newSold = 0;
        int numToShip = 0;

        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        if (!TextUtils.isEmpty(soldString)) {
            sold = Integer.parseInt(soldString);
        }
        if (!TextUtils.isEmpty(inStockString)) {
            inStock = Integer.parseInt(inStockString);
        }
        if (!TextUtils.isEmpty(newSoldStr)) {
            newSold = Integer.parseInt(newSoldStr);
        }
        if (!TextUtils.isEmpty(numToShipStr)) {
            numToShip = Integer.parseInt(numToShipStr);
        }

        //adjust inStock based on newSold and and numToShip
        int adjStock = inStock + numToShip - newSold;

        if (adjStock >= 0) {
            inStock = adjStock;
            newSold = 0;
            numToShip = 0;
            Toast.makeText(this, "Amt In Stock Adjusted", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "Cannot Sale more Than In Stock", Toast.LENGTH_LONG);
        }

        values.put(InvenEntry.PRICE, price);
        values.put(InvenEntry.TOTAL_ITEM_SALES, sold);
        values.put(InvenEntry.SUPPLY, inStock);
        values.put(InvenEntry.NEW_SOLD, newSold);
        values.put(InvenEntry.NUM_TO_SHIP, numToShip);
        values.put(InvenEntry.SUPPLIER_INFO, supplierInfo);

        //check to see if new product or not
        if (mCurrentUri == null) {
            Uri newUri = getContentResolver().insert(InvenEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error Saving Product", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            //This is an existing entry so update db with existing mCurrentUri
            int rowsUpdated = getContentResolver().update(mCurrentUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsUpdated == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Update Unsuccessful", Toast.LENGTH_LONG).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Update Successful!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void deleteProduct() {
        if (mCurrentUri == null) {
            Toast.makeText(this, "Product Not Yet Saved", Toast.LENGTH_LONG).show();
        } else {
            int deletedRows = getContentResolver().delete(mCurrentUri, null, null);

            if (deletedRows < 1) {
                Toast.makeText(this, "Error Deleting Product", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Product Deleted", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {InvenEntry._ID, InvenEntry.NAME, InvenEntry.PRICE, InvenEntry.SUPPLY,
                InvenEntry.TOTAL_ITEM_SALES, InvenEntry.SUPPLIER_INFO, InvenEntry.NUM_TO_ORDER, InvenEntry.NEW_SOLD
                , InvenEntry.NUM_TO_SHIP};
        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        if (cursor.moveToFirst()) {

            int supplyInt = cursor.getInt(cursor.getColumnIndex(SUPPLY));
            String nameStr = cursor.getString(cursor.getColumnIndex(NAME));
            int price = cursor.getInt(cursor.getColumnIndex(PRICE));
            int sold = cursor.getInt(cursor.getColumnIndex(TOTAL_ITEM_SALES));
            String supplierInfoStr = cursor.getString(cursor.getColumnIndex(SUPPLIER_INFO));
            String numToShip = cursor.getString(cursor.getColumnIndex(String.valueOf(NUM_TO_SHIP)));
            String newSold = cursor.getString(cursor.getColumnIndex(String.valueOf(NEW_SOLD)));
            String numToOrder = cursor.getString(cursor.getColumnIndex(String.valueOf(NUM_TO_ORDER)));


            mInStock.setText(String.valueOf(supplyInt));
            mProductName.setText(nameStr);
            mProductPrice.setText(String.valueOf(price));
            mUnitsSold.setText(String.valueOf(sold));
            mSupplierInfo.setText(supplierInfoStr);
            mNumToShip.setText(numToShip);
            mNewSold.setText(newSold);
            mNumToOrder.setText(numToOrder);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mInStock.setText("");
        mProductName.setText("");
        mProductPrice.setText(String.valueOf(""));
        mUnitsSold.setText(String.valueOf(""));
        mSupplierInfo.setText("");
        mNumToShip.setText("");
        mNewSold.setText("");
        mNumToOrder.setText("");

    }
}
