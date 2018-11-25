package aclass.android.adam.project4.data;

import android.provider.BaseColumns;

/**
 * Created by adam on 10/29/2017.
 */

public final class ShopItemDB {

    private ShopItemDB() {
    }

    public static class SheetsSync {
        public static final String TABLE_NAME = "sheets_sync";
        public static final String COLUMN_PURCHASE_ID = "purchase_id";
        public static final String COLUMN_SHEETS_ID = "sheets_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_SHEETS_ID + " INTEGER" + ")";
    }

    public static class OpenShiftSync {
        public static final String TABLE_NAME = "openshift_sync";
        public static final String COLUMN_PURCHASE_ID = "purchase_id";
        public static final String COLUMN_OPENSHIFT_ID = "openshift_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_OPENSHIFT_ID + " INTEGER" + ")";
    }

    public static class Shop implements BaseColumns, LocalBaseColumns {
        public static final String TABLE_NAME = "shop";
       // public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY , " + //AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ADDRESS + " TEXT" + ")";
    }

    public static class Category implements BaseColumns, LocalBaseColumns {
        public static final String TABLE_NAME = "category";
    //    public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY , " + //AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT " +
                ")";

        public static final String DELETE_ALL = "DELETE FROM " + TABLE_NAME;
    }

    public static class Denomination implements BaseColumns, LocalBaseColumns {
        public static final String TABLE_NAME = "denomination";
   //     public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY , " + //AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT " +
                ")";
    }

    public static class Purchase implements BaseColumns {
        public static final String TABLE_NAME = "purchase";
        public static final String COLUMN_ITEM_COMMON_NAME = "common_name";
        public static final String COLUMN_ITEM_SPECIFIC_NAME = "specific_name";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_SHOP_ID = "shop_id";
        public static final String COLUMN_DATE = "purchase_date";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUBQUANTITY = "subquantity";
        public static final String COLUMN_DENOMINATION_ID = "denomination_id";
        public static final String COLUMN_IS_SALE = "is_sale";
        public static final String COLUMN_NOTES = "notes";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY ," + //AUTOINCREMENT, " +
                COLUMN_ITEM_COMMON_NAME + " TEXT, " +
                COLUMN_ITEM_SPECIFIC_NAME + " TEXT, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_SHOP_ID + " INTEGER, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_QUANTITY + " REAL, " +
                COLUMN_SUBQUANTITY + " REAL NOT NULL DEFAULT 1.0, " +
                COLUMN_DENOMINATION_ID + " INTEGER, " +
                COLUMN_IS_SALE + " INTEGER, " +
                COLUMN_NOTES + " TEXT" + ")";
    }
}
