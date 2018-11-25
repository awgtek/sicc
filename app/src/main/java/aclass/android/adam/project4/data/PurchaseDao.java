package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.Editable;

import java.util.ArrayList;
import java.util.List;

import aclass.android.adam.project4.model.PurchaseItemCost;

/**
 * Created by adam on 11/4/2017.
 */

public class PurchaseDao {
    public static final String ITEM_FULL_NAME = "ITEM_FULL_NAME";
    public static final String ITEM_TOTAL_QUANTITY = "ITEM_TOTAL_QUANTITY";
    public static final String DENOMINATION_NAME = "DENOMINATION_NAME";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String SHOP_PLACE = "SHOP_PLACE";
    public static final String FMT_DATE = "FMT_DATE";
    public static final String SHOP_NAME = "SHOP_NAME";
    public static final String SHOP_ADDRESS = "SHOP_ADDRESS";
    public static final String LOW_PRICE_SHOP = "LOW_PRICE_SHOP";
    public static final String PRICE_RATIO = "PRICE_RATIO";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public PurchaseDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void deletePurchaseItem(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(ShopItemDB.Purchase.TABLE_NAME, ShopItemDB.Purchase._ID + " = ?",
                new String[] { String.valueOf(id)});
    }

    public long insertPurchase(String commonName, String specificName, Long categoryId, Long shopId, Double price,
                           Double quantity, Double subquantity, Long denominationId, Boolean isSale, Long date) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME, commonName);
        values.put(ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME, specificName);
        values.put(ShopItemDB.Purchase.COLUMN_CATEGORY_ID, categoryId);
        values.put(ShopItemDB.Purchase.COLUMN_SHOP_ID, shopId);
        values.put(ShopItemDB.Purchase.COLUMN_PRICE, price);
        values.put(ShopItemDB.Purchase.COLUMN_QUANTITY, quantity);
        if (subquantity != null) {
            values.put(ShopItemDB.Purchase.COLUMN_SUBQUANTITY, subquantity);
        }
        values.put(ShopItemDB.Purchase.COLUMN_DENOMINATION_ID, denominationId);
        values.put(ShopItemDB.Purchase.COLUMN_IS_SALE, isSale);
        values.put(ShopItemDB.Purchase.COLUMN_DATE, date);
        //values.put(ShopItemDB.Purchase._ID, 3l);
        return database.insert(ShopItemDB.Purchase.TABLE_NAME, null, values);
    }

    public Cursor getPurchase(long purchaseId) {
        String inTables = ShopItemDB.Purchase.TABLE_NAME + " JOIN " + ShopItemDB.Shop.TABLE_NAME + " ON " +
                ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_SHOP_ID + " = " +
                ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop._ID + " JOIN " + ShopItemDB.Denomination.TABLE_NAME + " ON " +
                ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_DENOMINATION_ID + " = " +
                ShopItemDB.Denomination.TABLE_NAME + "." + ShopItemDB.Denomination._ID + " JOIN " + ShopItemDB.Category.TABLE_NAME + " ON " +
                ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_CATEGORY_ID + " = " +
                ShopItemDB.Category.TABLE_NAME + "." + ShopItemDB.Category._ID;
        String[] projection = {ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase._ID,
                ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME, ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME,
                ShopItemDB.Purchase.COLUMN_PRICE, ShopItemDB.Purchase.COLUMN_QUANTITY, ShopItemDB.Purchase.COLUMN_SUBQUANTITY,
                ShopItemDB.Denomination.TABLE_NAME + "." + ShopItemDB.Denomination.COLUMN_NAME + " AS " + DENOMINATION_NAME,
                ShopItemDB.Category.TABLE_NAME + "." + ShopItemDB.Category.COLUMN_NAME + " AS " + CATEGORY_NAME,
                " strftime('%m/%d/%Y', " + ShopItemDB.Purchase.COLUMN_DATE + "/1000, 'unixepoch') AS " + ShopItemDB.Purchase.COLUMN_DATE,
                ShopItemDB.Purchase.COLUMN_IS_SALE, ShopItemDB.Purchase.COLUMN_NOTES,
                ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop.COLUMN_NAME +" AS " + SHOP_NAME,
                ShopItemDB.Shop.COLUMN_ADDRESS + " AS " + SHOP_ADDRESS
        };
        String selection = ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase._ID + " = " + purchaseId;
        return dbHelper.query(inTables, projection, selection, null, null, null, null);
    }

    public Cursor buildCursorBySearchString(String text) {
        String selection = ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + " like '%" + text + "%' OR "
                + ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME + " like '%" + text + "%' OR "
                + ShopItemDB.Category.TABLE_NAME + "." + ShopItemDB.Category.COLUMN_NAME + " like '%" + text + "%' ";
        String[] selectionArgs = {};
        return getPurchasesCursor(new SelectionParams(selection, selectionArgs, inTables()));
    }

    public SelectionParams buildSelectionParams(String commonName, String specificName) {
        String selection = ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + " = ? AND " + ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME + " = ?";
        String[] selectionArgs = {commonName, specificName};
        String inTables = inTables();

        return new SelectionParams(selection, selectionArgs, inTables);
    }

    @NonNull
    private String inTables() {
        return ShopItemDB.Purchase.TABLE_NAME
                    + " JOIN " + ShopItemDB.Shop.TABLE_NAME + " ON " +
                    ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_SHOP_ID + " = " +
                    ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop._ID
                    + " JOIN " + ShopItemDB.Category.TABLE_NAME + " ON " +
                    ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_CATEGORY_ID + " = " +
                    ShopItemDB.Category.TABLE_NAME + "." + ShopItemDB.Category._ID
                    + " JOIN " + ShopItemDB.Denomination.TABLE_NAME + " ON " +
                    ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_DENOMINATION_ID + " = " +
                    ShopItemDB.Denomination.TABLE_NAME + "." + ShopItemDB.Denomination._ID;
    }

    public Cursor getPurchasesCursor(SelectionParams selectionParams) {
        String sortOrder = PRICE_RATIO; //ShopItemDB.Purchase.COLUMN_DATE + " desc"; //todo add sort by price per denomination
        String[] projection = {ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase._ID,
                "(" + ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + " || ' ' || " + ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME +
                        ") AS " + ITEM_FULL_NAME,
               // " printf('%.2f'," + ShopItemDB.Purchase.COLUMN_PRICE + ") AS " + //not supported in older androids
                        ShopItemDB.Purchase.COLUMN_PRICE,
                "(" + ShopItemDB.Purchase.COLUMN_QUANTITY + " * ( CASE " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " WHEN 0 THEN 1 ELSE " +
                        ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " END ) " +
                        ") AS " + ITEM_TOTAL_QUANTITY,
                ShopItemDB.Category.TABLE_NAME + "." + ShopItemDB.Category.COLUMN_NAME + " AS " + CATEGORY_NAME,
                ShopItemDB.Denomination.TABLE_NAME + "." + ShopItemDB.Denomination.COLUMN_NAME + " AS " + DENOMINATION_NAME,
                        " strftime('%m/%d/%Y', " + ShopItemDB.Purchase.COLUMN_DATE + "/1000, 'unixepoch') AS " + FMT_DATE,
                ShopItemDB.Purchase.COLUMN_PRICE
                        + " / ( " + ShopItemDB.Purchase.COLUMN_QUANTITY +
                        " * ( CASE " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY
                        + " WHEN 0 THEN 1 ELSE "
                        + ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " END ) " +
                        " * ( CASE " + ShopItemDB.Denomination.TABLE_NAME + "." + ShopItemDB.Denomination.COLUMN_NAME
                        + " WHEN 'lb' THEN 16 ELSE 1"
                        +  " END ) " +
                        ") AS " + PRICE_RATIO,

                //ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop.COLUMN_NAME + " || ' ' || " + ShopItemDB.Shop.COLUMN_ADDRESS + " AS " + SHOP_PLACE};
                ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop.COLUMN_NAME + " AS " + SHOP_PLACE};
        return dbHelper.query(selectionParams.inTables, projection, selectionParams.selection, selectionParams.selectionArgs, null, null, sortOrder);
    }

    private class SelectionParams {
        public SelectionParams(String selection, String[] selectionArgs, String inTables) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            this.inTables = inTables;
        }
        String selection;
        String[] selectionArgs;

        String inTables;
    }

    public List<PurchaseItemCost> getPurchaseHistoryList() {
        List<PurchaseItemCost> purchaseList = new ArrayList<>();
        String inTables = ShopItemDB.Purchase.TABLE_NAME + " AS P JOIN " + ShopItemDB.Shop.TABLE_NAME +
                " ON P." + ShopItemDB.Purchase.COLUMN_SHOP_ID + " = " +
                ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop._ID + " JOIN " + ShopItemDB.Denomination.TABLE_NAME + " AS D ON " +
                "P." + ShopItemDB.Purchase.COLUMN_DENOMINATION_ID + " = D." + ShopItemDB.Denomination._ID;
        String sortOrder = ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + " asc, " + ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME + " asc";
        String groupBy = ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + ", " + ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME +
                ", " + ShopItemDB.Purchase.COLUMN_DENOMINATION_ID;
        Cursor cursor = dbHelper.query(inTables, new String[] {
                ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME,
                        ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME,
//                        ShopItemDB.Purchase.COLUMN_SUBQUANTITY,
//                        " (CASE WHEN " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY
//                                + " IS NULL OR " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY +
//                                " = 0 THEN 1 ELSE "
//                                + ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " END ) as subq",
                                "min("
                                + ShopItemDB.Purchase.COLUMN_PRICE + " / ( " + ShopItemDB.Purchase.COLUMN_QUANTITY +
                                " * ( CASE " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY
                                + " WHEN 0 THEN 1 ELSE "
                                + ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " END ) ) "
                                + ") ",
                        "max(" + ShopItemDB.Purchase.COLUMN_PRICE + " / ( " + ShopItemDB.Purchase.COLUMN_QUANTITY +
                                " * ( CASE " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " WHEN 0 THEN 1 ELSE " +
                                ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " END ) )) ",
                        "D." + ShopItemDB.Denomination.COLUMN_NAME + " AS " + DENOMINATION_NAME,
                        "(SELECT " + ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop.COLUMN_NAME + " FROM " +
                                ShopItemDB.Purchase.TABLE_NAME + " JOIN " + ShopItemDB.Shop.TABLE_NAME + " ON " +
                                ShopItemDB.Purchase.TABLE_NAME + "." + ShopItemDB.Purchase.COLUMN_SHOP_ID + " = " +
                                ShopItemDB.Shop.TABLE_NAME + "." + ShopItemDB.Shop._ID + " WHERE " +
                                ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + " = " + "P." +
                                ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME + " AND " +
                                ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME + " = " + "P." +
                                ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME + " AND " +
                                ShopItemDB.Purchase.COLUMN_DENOMINATION_ID + " = D._ID ORDER BY (" +
                                ShopItemDB.Purchase.COLUMN_PRICE + " / ( " + ShopItemDB.Purchase.COLUMN_QUANTITY +
                                " * ( CASE " + ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " WHEN 0 THEN 1 ELSE " +
                                ShopItemDB.Purchase.COLUMN_SUBQUANTITY + " END ) )) ASC LIMIT 1) AS " + LOW_PRICE_SHOP
                        ,
                        "COUNT(*)", "P." + ShopItemDB.Purchase._ID},
                null, null, groupBy, null, sortOrder);
        while (cursor.moveToNext()) {
            PurchaseItemCost purchaseItemCost = new PurchaseItemCost(cursor.getString(0), cursor.getString(1),
                    cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5));
            purchaseList.add(purchaseItemCost);
        }
        return purchaseList;
    }
}
