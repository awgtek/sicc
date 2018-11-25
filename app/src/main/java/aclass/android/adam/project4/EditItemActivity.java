package aclass.android.adam.project4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import aclass.android.adam.project4.data.CategoryDao;
import aclass.android.adam.project4.data.DenominationDao;
import aclass.android.adam.project4.data.PurchaseDao;
import aclass.android.adam.project4.data.ShopDao;
import aclass.android.adam.project4.data.ShopItemDB;

/**
 * Created by adam on 11/4/2017.
 */

public class EditItemActivity extends AppCompatActivity {

    AutoCompleteTextView commonName;
    AutoCompleteTextView specificName;
    Spinner category;
    Spinner shop;
    EditText price;
    EditText quantity;
    EditText subquantity;
    Spinner denomination;
    EditText date;
    CheckBox isSale;
    CategoryDao categoryDao;
    ShopDao shopDao;
    DenominationDao denominationDao;
    long selectedCategory, selectedShop, selectedDenomination;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_item_editor);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeActionContentDescription("Cancel");
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        commonName = (AutoCompleteTextView) findViewById(R.id.commonName);
        specificName = (AutoCompleteTextView) findViewById(R.id.specificName);
        category = (Spinner) findViewById(R.id.category);
        shop = (Spinner) findViewById(R.id.shop);
        price = (EditText) findViewById(R.id.price);
        quantity = (EditText) findViewById(R.id.quantity);
        subquantity = (EditText) findViewById(R.id.subquantity);
        denomination = (Spinner) findViewById(R.id.denomination);
        date = (EditText) findViewById(R.id.date);
        isSale = (CheckBox) findViewById(R.id.isSale);

        categoryDao = new CategoryDao(this);
        shopDao = new ShopDao(this);
        denominationDao = new DenominationDao(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        populateLookups();
    }

    private void populateLookups() {
        //categories
        String[] from = new String[]{ShopItemDB.Category.COLUMN_NAME};
        int[] to = new int[]{android.R.id.text1};

        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.dropdown_shop_item_view, categoryDao.getCategoryCursor(), from, to);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(sca);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = l;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for (int i = 0; i < category.getCount(); i++) {
            if (selectedCategory == category.getItemIdAtPosition(i)) {
                category.setSelection(i);
            }
        }


        //shops
        from = new String[]{ShopItemDB.Shop.COLUMN_NAME, ShopItemDB.Shop.COLUMN_ADDRESS};
        to = new int[]{android.R.id.text1, android.R.id.text2};
        sca = new SimpleCursorAdapter(this, R.layout.dropdown_shop_item_view, shopDao.getShops(), from, to);
        sca.setDropDownViewResource(R.layout.dropdown_shop_item_view);
        shop.setAdapter(sca);
        shop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedShop = l;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for (int i = 0; i < shop.getCount(); i++) {
            if (selectedShop == shop.getItemIdAtPosition(i)) {
                shop.setSelection(i);
            }
        }


        //denominations
        from = new String[]{ShopItemDB.Denomination.COLUMN_NAME};
        to = new int[]{android.R.id.text1};
        sca = new SimpleCursorAdapter(this, R.layout.dropdown_shop_item_view, denominationDao.getDenominations(), from, to);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        denomination.setAdapter(sca);
        denomination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDenomination = l;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for (int i = 0; i < denomination.getCount(); i++) {
            if (selectedDenomination == denomination.getItemIdAtPosition(i)) {
                denomination.setSelection(i);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void savePurchaseItem(View view) {
        String newItemPrice = price.getText().toString();
        String newItemQuantity = quantity.getText().toString();
        String newItemCommonName = commonName.getText().toString();
        String newItemSpecificName = specificName.getText().toString();
        if (newItemPrice.length() > 0
                && newItemQuantity.length() > 0
                && ((newItemCommonName.length() > 0)
                || (newItemSpecificName.length() > 0))) {
            try {
                PurchaseDao purchaseDao = new PurchaseDao(this);
                purchaseDao.insertPurchase(newItemCommonName, newItemSpecificName, category.getSelectedItemId(), shop.getSelectedItemId(), Double.valueOf(newItemPrice),
                        Double.valueOf(newItemQuantity),
                        Double.valueOf(subquantity.getText().toString().length() == 0? "0" : subquantity.getText().toString()),
                        denomination.getSelectedItemId(), isSale.isChecked(),
                        date.getText().toString().length() == 0? System.currentTimeMillis() / 1000 : dateFormat.parse(date.getText().toString()).getTime() / 1000);
                Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Insufficient data entered", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadLookupsActivity(View view) {
        Intent intent = new Intent(getBaseContext(), LookupsActivity.class);
        startActivity(intent);
    }
}