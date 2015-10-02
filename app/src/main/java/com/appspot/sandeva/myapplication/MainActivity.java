package com.appspot.sandeva.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity {
    static public DBHelper.Event event;
    private ListView listView;
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
    String format(double d) {
        return String.format("%02d°%02d'%02d\"", (int) d / 3600 % 30, (int) d / 60 % 60, (int) d % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultFont(this, "DEFAULT", "fonts/iskpota.ttf");
        setDefaultFont(this, "MONOSPACE", "fonts/iskpota.ttf");
        setDefaultFont(this, "SERIF", "fonts/iskpota.ttf");
        setDefaultFont(this, "SANS_SERIF", "fonts/iskpota.ttf");
        Log.d("============", "onCreate");

        // TODO location search feature
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName("Colombo", 1);
            Log.d("--------", addresses.toString());
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();


            }
            addresses = geocoder.getFromLocation(35.0, 135.0, 1);
            Log.d("--------", addresses.toString());
            if (addresses.size() > 0)
                Log.d("-------", addresses.get(0).getLocality());


        } catch (IOException e) {
            e.printStackTrace();
        }


        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale("si");
        res.updateConfiguration(conf, res.getDisplayMetrics());


        event = new DBHelper.Event(this, 0, "තත්කාල කේන්ද්\u200Dරය");

        setContentView(R.layout.planets);

        TextView tv=(TextView)findViewById(R.id.name);
        Typeface face=Typeface.createFromAsset(getAssets(), "fonts/iskpota.ttf");
        tv.setTypeface(face);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setContentView(R.layout.planets);
            for (Consts.Key x : Consts.getPlanets()) {
                TableRow tr = new TableRow(this);

                TextView tv = new TextView(this);
                tv.setText(x.getName());
                tr.addView(tv);

                tv = new TextView(this);
                tv.setText(Consts.Rashi[Calculator.getVarga(event.values, x, 1)]);
                tr.addView(tv);

                tv = new TextView(this);
                tv.setText(format(event.values.get(x)));
                tr.addView(tv);

                ((TableLayout) findViewById(R.id.table)).addView(tr);
            }
            return true;
        }
        if (id == R.id.action_chart) {
            ScrollView sv = new ScrollView(this);
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            sv.addView(ll);
            for (int i : VargaView.VARGAS.keySet()) {
                ll.addView(new VargaView(this, event.values, i));
                ll.addView(new TextView(this));
            }
            setContentView(sv);
            return true;
        }
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

    static public void setEvent(DBHelper.Event event) {
        MainActivity.event = event;
        Calculator.GetKendraNirayana(event.values);
    }
}
