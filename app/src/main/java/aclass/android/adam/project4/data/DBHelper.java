package aclass.android.adam.project4.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by adam on 10/29/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "ShopItem.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ShopItemDB.Category.CREATE_TABLE);
        sqLiteDatabase.execSQL(ShopItemDB.Denomination.CREATE_TABLE);
        sqLiteDatabase.execSQL(ShopItemDB.Shop.CREATE_TABLE);
        sqLiteDatabase.execSQL(ShopItemDB.Purchase.CREATE_TABLE);
        sqLiteDatabase.execSQL(ShopItemDB.SheetsSync.CREATE_TABLE);
        sqLiteDatabase.execSQL(ShopItemDB.OpenShiftSync.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopItemDB.Category.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopItemDB.Denomination.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopItemDB.Shop.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopItemDB.Purchase.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopItemDB.SheetsSync.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopItemDB.OpenShiftSync.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static void clearDB(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + ShopItemDB.Category.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ShopItemDB.Denomination.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ShopItemDB.Shop.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ShopItemDB.Purchase.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ShopItemDB.SheetsSync.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ShopItemDB.OpenShiftSync.TABLE_NAME);
    }

    /**
     * Generic query
     * @param inTables
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param sortOrder
     * @return Cursor
     */
    public Cursor query(String inTables, String[] projection, String selection,
                        String[] selectionArgs, String groupBy, String having, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(inTables);
        Cursor cursor = queryBuilder.query(getReadableDatabase(), projection, selection,
                selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }
}
