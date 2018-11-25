package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.HashSet;

/**
 * Created by adam on 12/12/2017.
 */

public class SheetsSyncDao extends LookupTableDao {

    public SheetsSyncDao(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return ShopItemDB.SheetsSync.TABLE_NAME;
    }

    @Override
    public void loadSavedSheetIds() {
        savedIds = new HashSet<>();
        Cursor cursor = dbHelper.query(getTableName(), new String[]{ShopItemDB.SheetsSync.COLUMN_SHEETS_ID}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            savedIds.add(cursor.getLong(0));
        }
    }

    public void insert(long purchaseId, long sheetsId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.SheetsSync.COLUMN_PURCHASE_ID, purchaseId);
        values.put(ShopItemDB.SheetsSync.COLUMN_SHEETS_ID, sheetsId);
        database.insert(ShopItemDB.SheetsSync.TABLE_NAME, null, values);
    }
}
