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

import aclass.android.adam.project4.data.DenominationDao;
import aclass.android.adam.project4.data.PurchaseDao;
import aclass.android.adam.project4.data.ShopItemDB;

/**
 * Created by adam on 10/29/2017.
 */

public class DenominationTabFragment extends Fragment implements View.OnClickListener {

    private EditText denominationNameEditText;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.denominations_gui, container, false);

        Button button = (Button) view.findViewById(R.id.saveDenominationsButton);
        button.setOnClickListener(this);
        ImageView imageView = (ImageView) view.findViewById(R.id.saveDenominationsCheckbox);
        imageView.setOnClickListener(this);

        denominationNameEditText = (EditText) view.findViewById(R.id.denominationNameEditText);

        DenominationDao denominationDao = new DenominationDao(getContext());
        Cursor cursor = denominationDao.getDenominations();

        adapter = new DenominationCursorAdapter(getContext(), R.layout.list_item_view, cursor,
                new String[]{ShopItemDB.Denomination.COLUMN_NAME }, new int[] {R.id.list_item});
        final ListView listView = (ListView) view.findViewById(R.id.denominationsListView);
        listView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveDenominationsButton:
            case R.id.saveDenominationsCheckbox:
                String newDenominationName = denominationNameEditText.getText().toString();
                if (null != newDenominationName && newDenominationName.length() > 0) {
                    DenominationDao denominationDao = new DenominationDao(getContext());
                    denominationDao.insertDenomination(newDenominationName);
                    Cursor cursor = denominationDao.getDenominations();
                    adapter.swapCursor(cursor);
                }
                //adapter.notifyDataSetChanged();
                denominationNameEditText.setText("");
                break;
        }
    }

    private static class DenominationCursorAdapter extends SimpleCursorAdapter {

        public DenominationCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
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
                    alert.setTitle("Delete Denomination");
                    alert.setMessage("Do you want to delete this denomination?");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cursor.moveToPosition(position);
                            int id = cursor.getInt(cursor.getColumnIndex(ShopItemDB.Denomination._ID));
                            DenominationDao denominationDao = new DenominationDao(context);
                            denominationDao.deleteDenomination(id);
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
