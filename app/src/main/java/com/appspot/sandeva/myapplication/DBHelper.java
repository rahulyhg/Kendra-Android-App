package com.appspot.sandeva.myapplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "event";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GMT = "gmt";
    public static final String TIMEZONE = "timezone";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME + " (" +
                        ID + " integer primary key, " +
                        NAME + " text, " +
                        GMT + " integer, " +
                        TIMEZONE + " integer, " +
                        LONGITUDE + " integer, " +
                        LATITUDE + " integer" +
                        ")"
        );
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData(int id) {
        return getReadableDatabase().rawQuery("select * from " + TABLE_NAME + " where " + ID + "=" + id + "", null);
    }

    public Event getEvent(int id) {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + TABLE_NAME + " where " + ID + "=" + id + "", null);
        cursor.moveToFirst();
        return getEventFromCursor(cursor);
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public ArrayList<Event> getAllCotacts() {
        ArrayList<Event> array_list = new ArrayList<Event>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Event e = getEventFromCursor(res);
            array_list.add(e);
            res.moveToNext();
        }
        return array_list;
    }

    private Event getEventFromCursor(Cursor res) {
        Event e = new Event(res.getInt(res.getColumnIndex(ID)),
                res.getString(res.getColumnIndex(NAME)));
        e.values.put(Consts.Key.GMT, res.getDouble(res.getColumnIndex(GMT)));
        e.values.put(Consts.Key.Timezone, res.getDouble(res.getColumnIndex(TIMEZONE)));
        e.values.put(Consts.Key.latitude, res.getDouble(res.getColumnIndex(LATITUDE)));
        e.values.put(Consts.Key.longitude, res.getDouble(res.getColumnIndex(LONGITUDE)));
        Calculator.GetKendraNirayana(e.values);
        return e;
    }

    static public class Event {
        int id;
        public String name;
        public Map<Consts.Key, Double> values;

        public Event(int id, String name) {
            this.id = id;
            this.name = name;
            values = new HashMap<Consts.Key, Double>();

            Calendar date = Calendar.getInstance();
            values.put(Consts.Key.GMT, (double) date.getTimeInMillis() - date.getTimeZone().getRawOffset());
            values.put(Consts.Key.Timezone, (double) date.getTimeZone().getRawOffset() / 1000);
            values.put(Consts.Key.latitude, .0);
            values.put(Consts.Key.longitude, .0);
            Calculator.GetKendraNirayana(values);

        }

        public String toString() {
            return name;
        }

        public boolean save(DBHelper dh) {
            SQLiteDatabase db = dh.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("gmt", values.get(Consts.Key.GMT));
            contentValues.put("timezone", values.get(Consts.Key.Timezone));
            contentValues.put("latitude", values.get(Consts.Key.latitude));
            contentValues.put("longitude", values.get(Consts.Key.longitude));
            if (id > 0)
                db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
            else
                this.id = (int) db.insert(TABLE_NAME, null, contentValues);
            return true;
        }
    }
}