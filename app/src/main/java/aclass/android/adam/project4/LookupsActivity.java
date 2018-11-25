package aclass.android.adam.project4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class LookupsActivity extends AppCompatActivity {

    public static final String CURRENT_MODE = "currentMode";
    public static final String EDIT_SHOPS_MODE = "editShopsMode";

    private FragmentTabHost fragmentTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookups);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("Categories").setIndicator(
                buildCustomIndicator("Categories",R.drawable.shades)), CategoryTabFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("Denominations").setIndicator(
                buildCustomIndicator("Denominations",R.drawable.scale)), DenominationTabFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("Shops").setIndicator(
                buildCustomIndicator("Shops",
                        R.drawable.shopping
                )),
                ShopTabFragment.class, null);
        fragmentTabHost.setCurrentTab(0);
        ((LinearLayout.LayoutParams)fragmentTabHost.getTabWidget().getChildAt(0).getLayoutParams()).weight = 0;
        ((LinearLayout.LayoutParams)fragmentTabHost.getTabWidget().getChildAt(1).getLayoutParams()).weight = 0;
        ((LinearLayout.LayoutParams)fragmentTabHost.getTabWidget().getChildAt(2).getLayoutParams()).weight = 0;
        ((LinearLayout.LayoutParams)fragmentTabHost.getTabWidget().getChildAt(0).getLayoutParams()).width = 150;
        ((LinearLayout.LayoutParams)fragmentTabHost.getTabWidget().getChildAt(1).getLayoutParams()).width = 180;
        ((LinearLayout.LayoutParams)fragmentTabHost.getTabWidget().getChildAt(2).getLayoutParams()).width = 130;
        View child = fragmentTabHost.getTabWidget().getChildAt(1);
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                setTabColors();

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        setTabColors();

    }

    private void setTabColors() {
        for (int i = 0; i < fragmentTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) fragmentTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_text); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#63605b"));
        }
        TextView tv = (TextView) fragmentTabHost.getCurrentTabView().findViewById(R.id.tab_text); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public View buildCustomIndicator(String inText, int resId) {
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
        tabIndicator.setBackground(null);
        ((TextView)tabIndicator.findViewById(R.id.tab_text)).setText(inText);
        ((ImageView)tabIndicator.findViewById(R.id.tab_icon)).setImageResource(resId);
        return tabIndicator;
    }
}
