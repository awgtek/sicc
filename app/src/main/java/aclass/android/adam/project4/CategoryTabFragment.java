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

import aclass.android.adam.project4.data.CategoryDao;
import aclass.android.adam.project4.data.ShopDao;
import aclass.android.adam.project4.data.ShopItemDB;

/**
 * Created by adam on 10/29/2017.
 */

public class CategoryTabFragment extends Fragment implements View.OnClickListener {

    private EditText categoryNameEditText;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_gui, container, false);

        Button button = (Button) view.findViewById(R.id.saveCategoriesButton);
        button.setOnClickListener(this);
        ImageView imageView = (ImageView) view.findViewById(R.id.saveCategoryCheckbox);
        imageView.setOnClickListener(this);

        categoryNameEditText = (EditText) view.findViewById(R.id.categoryNameEditText);

        CategoryDao categoryDao = new CategoryDao(getContext());
        Cursor cursor = categoryDao.getCategoryCursor();

        adapter = new CategoryCursorAdapter(getContext(), R.layout.list_item_view, cursor,
                new String[]{ShopItemDB.Category.COLUMN_NAME }, new int[] {R.id.list_item});
        final ListView listView = (ListView) view.findViewById(R.id.categoriesListView);
        listView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveCategoriesButton :
            case R.id.saveCategoryCheckbox :
                String newCategoryName = categoryNameEditText.getText().toString();
                if (null != newCategoryName && newCategoryName.length() > 0) {
                    CategoryDao categoryDao = new CategoryDao(getContext());
                    categoryDao.insertCategory(newCategoryName);
                    Cursor cursor = categoryDao.getCategoryCursor();
                    adapter.swapCursor(cursor);
                }
                categoryNameEditText.setText("");
                break;
        }
    }

    private static class CategoryCursorAdapter extends SimpleCursorAdapter {

        public CategoryCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
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
                    alert.setTitle("Delete Category");
                    alert.setMessage("Do you want to delete this category?");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cursor.moveToPosition(position);
                            int id = cursor.getInt(cursor.getColumnIndex(ShopItemDB.Category._ID));
                            CategoryDao categoryDao = new CategoryDao(context);
                            categoryDao.deleteCategory(id);
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
