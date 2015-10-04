package com.appspot.sandeva.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends Activity {

    private DBHelper mydb;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mydb = new DBHelper(this);
        final ArrayList<DBHelper.Event> array_list = mydb.getAllCotacts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long row_id) {
                Log.d("Position-->", Long.toString(position));
                Log.d("ROW_ID-->", Long.toString(row_id));
                MainActivity.activity.setEvent(array_list.get(position));
                MainActivity main = MainActivity.activity;
                main.viewPager.setAdapter(new TabsPagerAdapter(main.getSupportFragmentManager()));
                finish();
            }
        });

    }
}
