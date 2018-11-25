package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by adam on 10/29/2017.
 */

public class CategoryDao extends LookupTableDao {

    public CategoryDao(Context context) {
        super(context);
    }

    public Cursor getCategoryCursor() {
        String sortOrder = ShopItemDB.Category.COLUMN_NAME + " asc";
        return dbHelper.query(ShopItemDB.Category.TABLE_NAME, new String[] {ShopItemDB.Category._ID,
                ShopItemDB.Category.COLUMN_NAME}, null, null, null, null, sortOrder);
    }

    public long insertCategory(String categoryName) {
        //if (true) throw new RuntimeException("obsolete. cannot insertLookup category");
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Category.COLUMN_NAME, categoryName);
        long newRowId = database.insert(ShopItemDB.Category.TABLE_NAME, null, values);
        return newRowId;
    }

    public void insertCategory(int id, String categoryName) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Category.COLUMN_NAME, categoryName);
        values.put(ShopItemDB.Category._ID, id);
        long newRowId = database.insert(ShopItemDB.Category.TABLE_NAME, null, values);
    }

    public void deleteCategory(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(ShopItemDB.Category.TABLE_NAME, ShopItemDB.Category._ID + " = ?",
                new String[] { String.valueOf(id)});
    }

    public void deleteAll() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(ShopItemDB.Category.DELETE_ALL);
    }

    @Override
    public String getTableName() {
        return ShopItemDB.Category.TABLE_NAME;
    }
}
