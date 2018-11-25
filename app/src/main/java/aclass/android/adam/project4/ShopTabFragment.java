package aclass.android.adam.project4;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import aclass.android.adam.project4.data.ShopDao;
import aclass.android.adam.project4.data.ShopItemDB;

/**
 * Created by adam on 10/29/2017.
 */

public class ShopTabFragment extends Fragment implements View.OnClickListener {
    private EditText shopNameEditText;
    private EditText shopAddressEditText;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_gui, container, false);

        Button button = (Button) view.findViewById(R.id.saveShopButton);
        button.setOnClickListener(this);
        ImageView imageView = (ImageView) view.findViewById(R.id.saveShopCheckbox);
        imageView.setOnClickListener(this);

        shopNameEditText = (EditText) view.findViewById(R.id.shopNameEditText);
        shopAddressEditText = (EditText) view.findViewById(R.id.shopAddressEditText);

        ShopDao shopDao = new ShopDao(getContext());
        Cursor cursor = shopDao.getShops();

        adapter = new ShopTabFragment.ShopCursorAdapter(getContext(), R.layout.list_item_shop_view, cursor,
                new String[]{ShopItemDB.Shop.COLUMN_NAME, ShopItemDB.Shop.COLUMN_ADDRESS },
                new int[] {R.id.list_item_shop_name, R.id.list_item_shop_address});
        final ListView listView = (ListView) view.findViewById(R.id.shopsListView);
        listView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveShopButton:
            case R.id.saveShopCheckbox:
                String newShopName = shopNameEditText.getText().toString();
                String newShopAddress = shopAddressEditText.getText().toString();
                if (null != newShopName && newShopName.length() > 0) {
                    ShopDao shopDao = new ShopDao(getContext());
                    shopDao.insertShop(newShopName, newShopAddress);
                    Cursor cursor = shopDao.getShops();
                    adapter.swapCursor(cursor);
                }
                shopNameEditText.setText("");
                shopAddressEditText.setText("");
                break;
        }
    }

    private static class ShopCursorAdapter extends SimpleCursorAdapter {

        public ShopCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            ImageView imageView = view.findViewById(R.id.btndelete);
            final int position = cursor.getPosition();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete Shop");
                    alert.setMessage("Do you want to delete this shop?");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cursor.moveToPosition(position);
                            int id = cursor.getInt(cursor.getColumnIndex(ShopItemDB.Shop._ID));
                            ShopDao shopDao = new ShopDao(context);
                            shopDao.deleteShop(id);
                            cursor.requery();
                            notifyDataSetChanged();
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
            super.bindView(view, context, cursor);
        }
    }
}
