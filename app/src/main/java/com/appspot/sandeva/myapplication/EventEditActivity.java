package com.appspot.sandeva.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

public class EventEditActivity extends Activity {
    private DBHelper mydb;

    TextView name;
    NumberPicker tz_s, tz_h, tz_m, lt_s, lt_d, lt_m, lg_s, lg_d, lg_m;
    DatePicker dp;
    TimePicker tp;

    void makeSgn(NumberPicker s, NumberPicker d, NumberPicker m, int max, String[] values) {
        d.setMaxValue(max);
        m.setMaxValue(59);
        s.setMaxValue(1);
        s.setDisplayedValues(values);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);


        setContentView(R.layout.activity_display_contact);
        name = (TextView) findViewById(R.id.editTextName);
        tz_s = (NumberPicker) findViewById(R.id.tz_s);
        tz_h = (NumberPicker) findViewById(R.id.tz_h);
        tz_m = (NumberPicker) findViewById(R.id.tz_m);
        lt_s = (NumberPicker) findViewById(R.id.lt_s);
        lt_d = (NumberPicker) findViewById(R.id.lt_d);
        lt_m = (NumberPicker) findViewById(R.id.lt_m);
        lg_s = (NumberPicker) findViewById(R.id.lg_s);
        lg_d = (NumberPicker) findViewById(R.id.lg_d);
        lg_m = (NumberPicker) findViewById(R.id.lg_m);
        dp = (DatePicker) findViewById(R.id.datePicker1);
        tp = (TimePicker) findViewById(R.id.timePicker2);

        makeSgn(tz_s, tz_h, tz_m, 12, new String[]{"+", "-"});
        makeSgn(lt_s, lt_d, lt_m, 90, new String[]{"උ", "ද"});
        makeSgn(lg_s, lg_d, lg_m, 180, new String[]{"නැ", "බ"});

        Calendar c = Calendar.getInstance();

        Map<Consts.Key, Double> values = MainActivity.activity.event.values;
        Log.d(values.get(Consts.Key.GMT).toString(), values.get(Consts.Key.Timezone).toString());
        c.setTimeInMillis((long)(values.get(Consts.Key.GMT) + 1000 * values.get(Consts.Key.Timezone)));

        name.setText(MainActivity.activity.event.name);
        dp.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        tp.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(c.get(Calendar.MINUTE));

        readColumn(tz_s, tz_h, tz_m, Consts.Key.Timezone);
        readColumn(lt_s, lt_d, lt_m, Consts.Key.latitude);
        readColumn(lg_s, lg_d, lg_m, Consts.Key.longitude);
    }

    private void readColumn(NumberPicker s, NumberPicker d, NumberPicker m, Consts.Key key) {
        int val = MainActivity.activity.event.values.get(key).intValue();
        s.setValue(val > 0 ? 0 : 1);
        d.setValue(Math.abs(val) / 3600);
        m.setValue(Math.abs(val) / 60 % 60);
    }

    double getVal(NumberPicker s, NumberPicker d, NumberPicker m) {
        return (double)(1 - s.getValue() * 2) * (d.getValue() * 3600 + m.getValue() * 60);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_contact, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Delete_Contact:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(MainActivity.activity.event.id);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());
        MainActivity.activity.event.name = name.getText().toString();
        double tz = getVal(tz_s, tz_h, tz_m);
        Map<Consts.Key, Double> values = MainActivity.activity.event.values;
        values.put(Consts.Key.GMT, (double) calendar.getTimeInMillis() - tz * 1000);
        values.put(Consts.Key.Timezone, tz);
        values.put(Consts.Key.latitude, getVal(lt_s, lt_d, lt_m));
        values.put(Consts.Key.longitude, getVal(lg_s, lg_d, lg_m));
        MainActivity.activity.event.save(mydb);
        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
        finish();
    }
}
