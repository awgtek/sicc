package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adam on 10/29/2017.
 */

public class ShopDao extends LookupTableDao {

    public ShopDao(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return ShopItemDB.Shop.TABLE_NAME;
    }

    public Cursor getShops() {
        String sortOrder = ShopItemDB.Shop.COLUMN_NAME + " asc";
        return dbHelper.query(ShopItemDB.Shop.TABLE_NAME, new String[] {ShopItemDB.Shop._ID,
                ShopItemDB.Shop.COLUMN_NAME, ShopItemDB.Shop.COLUMN_ADDRESS}, null, null, null, null, sortOrder);
    }

    public void insertShop(String shopName, String shopAddress) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Shop.COLUMN_NAME, shopName);
        values.put(ShopItemDB.Shop.COLUMN_ADDRESS, shopAddress);
        database.insert(ShopItemDB.Shop.TABLE_NAME, null, values);
    }

    public void insertShop(long id, String shopName, String shopAddress) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Shop._ID, id);
        values.put(ShopItemDB.Shop.COLUMN_NAME, shopName);
        values.put(ShopItemDB.Shop.COLUMN_ADDRESS, shopAddress);
        database.insert(ShopItemDB.Shop.TABLE_NAME, null, values);
    }

    public void deleteShop(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(ShopItemDB.Shop.TABLE_NAME, ShopItemDB.Shop._ID + " = ?",
                new String[] { String.valueOf(id)});
    }

}
