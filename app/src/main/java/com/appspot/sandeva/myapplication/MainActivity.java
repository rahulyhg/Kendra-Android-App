package com.appspot.sandeva.myapplication;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Field;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {
    static public MainActivity activity;
    public DBHelper.Event event;
    public ViewPager viewPager;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "කේන්ද්\u200Dරය", "ග්\u200Dරහයෝ" };
    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setDefaultFont(this, "DEFAULT", "fonts/iskpota.ttf");
        setDefaultFont(this, "MONOSPACE", "fonts/iskpota.ttf");
        setDefaultFont(this, "SERIF", "fonts/iskpota.ttf");
        setDefaultFont(this, "SANS_SERIF", "fonts/iskpota.ttf");

        event = new DBHelper.Event(this, 0, "තත්කාල කේන්ද්\u200Dරය");
        setContentView(R.layout.activity_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_data) {
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", event.id);

            Intent intent = new Intent(getApplicationContext(), EventEditActivity.class);
            intent.putExtras(dataBundle);

            startActivity(intent);
            return true;
        }
        if (id == R.id.name_list) {
            startActivity(new Intent(this, ListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setEvent(DBHelper.Event event) {
        MainActivity.activity.event = event;
        Calculator.GetKendraNirayana(event.values);
    }
}
