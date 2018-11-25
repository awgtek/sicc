package aclass.android.adam.project4.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by adam on 12/16/2017.
 */

public abstract class LookupTableDao {
    protected DBHelper dbHelper;
    protected HashSet<Long> savedIds;
    //private SQLiteDatabase db;
    private Map<String, Long> nameIdMap = new HashMap<>();


    public LookupTableDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public Long getId(String name) {
        String[] projection = new String[]{
                BaseColumns._ID, LocalBaseColumns.COLUMN_NAME};
        String selection = LocalBaseColumns.COLUMN_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = dbHelper.query(getTableName(), projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        } else {
            return null;
        }


//        String selection = ShopItemDB.Category.COLUMN_NAME + " = ?";
//        String[] selectionArgs = {name};
//        if (nameIdMap.isEmpty()) {
//            synchronized (nameIdMap) {
//                if (nameIdMap.isEmpty()) {
//                    String[] projection = new String[]{
//                            BaseColumns._ID, LocalBaseColumns.COLUMN_NAME};
//                    Cursor cursor = dbHelper.query(getTableName(), projection, null, null, null, null, null);
//                    while (cursor.moveToNext()) {
//                        nameIdMap.put(cursor.getString(1), cursor.getLong(0));
//                    }
//                }
//            }
//        }
//        return nameIdMap.get(name);
    }

    public boolean isIdSaved(long rowId) {
        boolean isSaved = savedIds.contains(rowId);
        return isSaved;
    }

    public void loadSavedSheetIds() {
        savedIds = new HashSet<>();
        Cursor cursor = dbHelper.query(getTableName(), new String[]{BaseColumns._ID}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            savedIds.add(cursor.getLong(0));
        }
    }

    public long insertLookup(String name) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocalBaseColumns.COLUMN_NAME, name);
        long newRowId = database.insert(getTableName(), null, values);
        return newRowId;
    }

    public long insertLookup(long id, String name) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocalBaseColumns.COLUMN_NAME, name);
        values.put(BaseColumns._ID, id);
        long newRowId = database.insert(getTableName(), null, values);
        return newRowId;
    }

    public abstract String getTableName();
}
