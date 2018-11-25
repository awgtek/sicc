package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;

/**
 * Created by adam on 12/12/2017.
 */

public class OpenShiftSyncDao extends LookupTableDao {

    public OpenShiftSyncDao(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return ShopItemDB.OpenShiftSync.TABLE_NAME;
    }

    @Override
    public void loadSavedSheetIds() {
        savedIds = new HashSet<>();
        Cursor cursor = dbHelper.query(getTableName(), new String[]{ShopItemDB.OpenShiftSync.COLUMN_OPENSHIFT_ID}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            savedIds.add(cursor.getLong(0));
        }
    }

    public void ensureTable() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(ShopItemDB.OpenShiftSync.CREATE_TABLE);
    }

    public void insert(long purchaseId, long osId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopItemDB.OpenShiftSync.COLUMN_PURCHASE_ID, purchaseId);
        values.put(ShopItemDB.OpenShiftSync.COLUMN_OPENSHIFT_ID, osId);
        database.insert(ShopItemDB.OpenShiftSync.TABLE_NAME, null, values);
    }
}
