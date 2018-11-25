package aclass.android.adam.project4;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import aclass.android.adam.project4.data.PurchaseDao;
import aclass.android.adam.project4.data.ShopItemDB;
import aclass.android.adam.project4.model.PurchaseItemCost;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView actv;
    private PurchaseDao purchaseDao;
    private PurchaseItemCost currentPurchaseItemCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        purchaseDao = new PurchaseDao(this);

    }

    private void setupSearchAutocomplete() {
//        final ArrayAdapter<PurchaseItemCost> adapter = new ArrayAdapter<PurchaseItemCost>(this,
//                R.layout.select_autocomplete_purchase_item, purchaseDao.getPurchaseHistoryList());

        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        Point pointSize = new Point();
//        getWindowManager().getDefaultDisplay().getSize(pointSize);
//        actv.setDropDownWidth(pointSize.x);
//        actv.setThreshold(3);
//        actv.setAdapter(adapter);
//        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseItemCost purchaseItemCost = (PurchaseItemCost) adapterView.getItemAtPosition(i);
//                currentPurchaseItemCost = purchaseItemCost;
//                buildPurchaseItemListView(purchaseItemCost);
//                actv.setText("");
//            }
//        });
//
//        actv.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                actv.showDropDown();
//                return false;
//            }
//        });

        actv.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //Toast.makeText(MainActivity.this, actv.getText(), Toast.LENGTH_SHORT).show();
                    final Cursor cursor = purchaseDao.buildCursorBySearchString(actv.getText().toString());
                    buildPurchaseItemListView(cursor);
                    return true;
                }
                return false;
            }
        });
    }

    private void buildPurchaseItemListView(PurchaseItemCost purchaseItemCost) {
        final Cursor cursor = purchaseDao.getPurchasesCursor(purchaseDao
                .buildSelectionParams(purchaseItemCost.getCommonName(), purchaseItemCost.getSpecificName()));
        buildPurchaseItemListView(cursor);
    }
    private void buildPurchaseItemListView(Cursor cursor) {
        String[] from = {PurchaseDao.ITEM_FULL_NAME, ShopItemDB.Purchase.COLUMN_PRICE, PurchaseDao.PRICE_RATIO, PurchaseDao.ITEM_TOTAL_QUANTITY,
                PurchaseDao.DENOMINATION_NAME, PurchaseDao.FMT_DATE, PurchaseDao.SHOP_PLACE, PurchaseDao.CATEGORY_NAME};//ShopItemDB.Purchase.COLUMN_DATE, PurchaseDao.SHOP_PLACE};
        int[] to = {R.id.item_full_name, R.id.purchase_item_price, R.id.purchase_item_price_ratio, R.id.purchase_item_total_quantity,
                R.id.purchase_item_denomination, R.id.purchase_item_date, R.id.shop_place, R.id.purchase_item_category};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.purchase_item, cursor, from, to);
        final ListView listView = (ListView) MainActivity.this.findViewById(R.id.purchasesListView);
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, final long l) {
                Cursor detailsCursor = purchaseDao.getPurchase(l);
                detailsCursor.moveToNext();
                int nameColumnIndex = detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_ITEM_COMMON_NAME);
                String itemCommonName = "";
                try {
                    itemCommonName = detailsCursor.getString(nameColumnIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nameColumnIndex = detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_ITEM_SPECIFIC_NAME);
                String itemSpecificName = "";
                try {
                    itemSpecificName = detailsCursor.getString(nameColumnIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.purchase_item_details_dialog);
                dialog.setTitle("Shop Item Details");

                TableLayout table = (TableLayout) dialog.findViewById(R.id.detailsItemTable);

                if (itemCommonName.length() > 0) {
                    TextView textView = (TextView) dialog.findViewById(R.id.detailsItemCommonName);
                    textView.setText(itemCommonName);
                } else {
                    TableRow row = (TableRow) dialog.findViewById(R.id.detailsItemCommonNameRow);
                    table.removeView(row);
                }
                if (itemSpecificName.length() > 0) {
                    TextView textView = (TextView) dialog.findViewById(R.id.detailsItemSpecificName);
                    textView.setText(itemSpecificName);
                } else {
                    TableRow row = (TableRow) dialog.findViewById(R.id.detailsItemSpecificNameRow);
                    table.removeView(row);
                }
                TextView textView = (TextView) dialog.findViewById(R.id.detailsItemCategory);
                String category = detailsCursor.getString(detailsCursor.getColumnIndex(PurchaseDao.CATEGORY_NAME));
                textView.setText(category);

                textView = (TextView) dialog.findViewById(R.id.detailsItemShopName);
                String shopName = detailsCursor.getString(detailsCursor.getColumnIndex(PurchaseDao.SHOP_NAME));
                textView.setText(shopName);

                String shopAddress = detailsCursor.getString(detailsCursor.getColumnIndex(PurchaseDao.SHOP_ADDRESS));
                if (null != shopAddress && shopAddress.length() > 0) {
                    textView = (TextView) dialog.findViewById(R.id.detailsItemShopAddress);
                    textView.setText(shopAddress);
                } else {
                    TableRow row = (TableRow) dialog.findViewById(R.id.detailsItemShopAddressRow);
                    table.removeView(row);
                }

                textView = (TextView) dialog.findViewById(R.id.detailsItemPrice);
                double price = detailsCursor.getDouble(detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_PRICE));
                textView.setText(String.format("$%.2f", price));

                int isOnSale = detailsCursor.getInt(detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_IS_SALE));
                if (isOnSale > 0) {
                    textView = (TextView) dialog.findViewById(R.id.detailsItemIsOnSale);
                    textView.setText("Yes");
                } else {
                    TableRow row = (TableRow) dialog.findViewById(R.id.detailsItemIsOnSaleRow);
                    table.removeView(row);
                }

                double quantity = detailsCursor.getDouble(detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_QUANTITY));
                double subquantity = detailsCursor.getDouble(detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_SUBQUANTITY));
                String denomination = detailsCursor.getString(detailsCursor.getColumnIndex(PurchaseDao.DENOMINATION_NAME));
                textView = (TextView) dialog.findViewById(R.id.detailsItemQuantity);
                DecimalFormat decimalFormat = new DecimalFormat("#.#####");
                String quantityText = decimalFormat.format(quantity) +
                        ((subquantity > 0d)? " * " + decimalFormat.format(subquantity) : "") + " " + denomination;
                textView.setText(quantityText);

                textView = (TextView) dialog.findViewById(R.id.detailsItemDate);
                String date = detailsCursor.getString(detailsCursor.getColumnIndex(ShopItemDB.Purchase.COLUMN_DATE));
                textView.setText(date);

                ImageView cancelButton = (ImageView) dialog.findViewById(R.id.purchaseItemCancelDialogButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ImageView deleteButton = (ImageView) dialog.findViewById(R.id.purchaseItemDeleteDialogButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Delete Purchase Item");
                        alert.setMessage("Do you want to delete this purchase record?");
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PurchaseDao purchaseDao = new PurchaseDao(MainActivity.this);
                                purchaseDao.deletePurchaseItem(l);
                                setupAutoCompleteLists();
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.show();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAutoCompleteLists();
    }

    private void setupAutoCompleteLists() {
        setupSearchAutocomplete();
        ListView listView = (ListView) MainActivity.this.findViewById(R.id.purchasesListView);
        if (currentPurchaseItemCost != null) {
            buildPurchaseItemListView(currentPurchaseItemCost);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_lookups) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadLookupsActivity(View view) {


        Intent intent = new Intent(getBaseContext(), LookupsActivity.class);
        intent.putExtra(LookupsActivity.CURRENT_MODE, LookupsActivity.EDIT_SHOPS_MODE);
        startActivity(intent);
    }

    public void loadNewItem(View view) {
        Intent intent = new Intent(getBaseContext(), EditItemActivity.class);
        intent.putExtra(LookupsActivity.CURRENT_MODE, LookupsActivity.EDIT_SHOPS_MODE);
        startActivity(intent);
    }

    public void syncToGoogleSheets(View view) {
        Intent intent = new Intent(getBaseContext(), SheetsSyncActivity.class);
        //intent.putExtra(LookupsActivity.CURRENT_MODE, LookupsActivity.EDIT_SHOPS_MODE);
        startActivityForResult(intent, SheetsSyncActivity.REQUEST_SYNC_SHEETS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
