package com.example.micha.inventory.Data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by micha on 12/5/2016.
 */

public final class InvenContract {


public final static String CONTENT_AUTHORITY = "com.example.micha.inventory";
    public final static Uri BASE_CONTENT_URI =
            Uri.parse("content://" + CONTENT_AUTHORITY);
    // This constant stores the path for each of the tables which will be appended to the base content URI.
    public static final String PATH_INVEN = "inventory";

    //private constructor as no object ever created
    private InvenContract() {
    }

public static final class InvenEntry implements BaseColumns{

    //usable uri for accessing db

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_INVEN);

    /** Name of database table for inventory */
    public final static String TABLE_NAME = "inventory";

    /**
     * Unique ID number for the inventory item (only for use in the database table).
     *
     * Type: INTEGER
     */
public final static String _ID = BaseColumns._ID;

    public final static String NAME = "name";

    public final static String SUPPLY = "supply";

    public final static String PRICE = "price";

    public final static String TOTAL_ITEM_SALES = "total_sales";

    public final static String SUPPLIER_INFO = "supplier_info";

    public final static String NUM_TO_SHIP = "number_to_ship";

    public final static String NEW_SOLD = "new_sold";

    public final static String NUM_TO_ORDER = "num_to_order";

    }

}
