package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adam on 10/29/2017.
 */

public class DenominationDao extends LookupTableDao {

    public DenominationDao(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return ShopItemDB.Denomination.TABLE_NAME;
    }


    public Cursor getDenominations() {
        String sortOrder = ShopItemDB.Denomination.COLUMN_NAME + " asc";
        return dbHelper.query(ShopItemDB.Denomination.TABLE_NAME, new String[]{ShopItemDB.Denomination._ID,
                ShopItemDB.Denomination.COLUMN_NAME}, null, null, null, null, sortOrder);
    }

    public void insertDenomination(String denominationName) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Denomination.COLUMN_NAME, denominationName);
        long newRowId = database.insert(ShopItemDB.Denomination.TABLE_NAME, null, values);
    }

    public void insertDenomination(long id, String denominationName) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.Denomination.COLUMN_NAME, denominationName);
        values.put(ShopItemDB.Denomination._ID, id);
        long newRowId = database.insert(ShopItemDB.Denomination.TABLE_NAME, null, values);
    }

    public void deleteDenomination(long id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(ShopItemDB.Denomination.TABLE_NAME, ShopItemDB.Denomination._ID + " = ?",
                new String[]{String.valueOf(id)});
    }


}
