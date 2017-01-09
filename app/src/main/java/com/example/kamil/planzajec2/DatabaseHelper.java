package com.example.kamil.planzajec2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.kamil.planzajec2.Zajecie.dodajMinuty;

/**
 * Created by kamil on 18.11.16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "plan_zajec2.sqlite";
    // Contacts table name
    private static final String TABLE = "plan";
    // Shops Table Columns names
    private static final String ID = "id";
    private static final String DZIEN = "dzien";
    private static final String PRZEDMIOT = "przedmiot";
    private static final String GODZINA = "godzina";
    private static final String SALA = "sala";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE + "("
                + ID + " INTEGER PRIMARY KEY," + DZIEN + " INTEGER,"
                + PRZEDMIOT + " TEXT," + GODZINA + " TIMESTAMP,"
                + SALA + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // Creating tables again
        onCreate(db);
    }

    public void addZajecie(Zajecie shop) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DZIEN, shop.getDzien());
        values.put(PRZEDMIOT, shop.getPrzedmiot());
        values.put(GODZINA, shop.getGodzina());
        values.put(SALA, shop.getSala());

        // Inserting Row
        db.insert(TABLE, null, values);
        db.close();
    }

    public Zajecie getZajecie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{ID,
                        DZIEN, PRZEDMIOT, GODZINA, SALA}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        assert cursor != null;
        Zajecie zajecie = new Zajecie(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
                cursor.getString(4));
  
        cursor.close();
        return zajecie;
    }

    public List<Zajecie> getAllZajecia() throws ParseException {
        List<Zajecie> zajeciaList = new ArrayList<Zajecie>();

        String selectQuery = "SELECT * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Zajecie zajecie = new Zajecie();
                zajecie.setId(Integer.parseInt(cursor.getString(0)));
                zajecie.setDzien(Integer.parseInt(cursor.getString(1)));
                zajecie.setPrzedmiot(cursor.getString(2));
                zajecie.setGodzina(cursor.getString(3));
                zajecie.setSala(cursor.getString(4));
                // Adding contact to list
                zajeciaList.add(zajecie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return zajeciaList;
    }

    public List<Zajecie> getZajecia(int dzien) throws ParseException {
        List<Zajecie> zajeciaList = new ArrayList<Zajecie>();

        String selectQuery = "SELECT * FROM " + TABLE + " WHERE " + DZIEN + "=" + dzien
                + " ORDER BY " + GODZINA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Zajecie shop = new Zajecie();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setDzien(Integer.parseInt(cursor.getString(1)));
                shop.setPrzedmiot(cursor.getString(2));
                shop.setGodzina(cursor.getString(3));
                shop.setSala(cursor.getString(4));

                zajeciaList.add(shop);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return zajeciaList;
    }

    public int getZajeciaCount() {
        String countQuery = "SELECT * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int updateZajecie(Zajecie zajecie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DZIEN, zajecie.getDzien());
        values.put(PRZEDMIOT, zajecie.getPrzedmiot());
        values.put(GODZINA, zajecie.getGodzina());
        values.put(SALA, zajecie.getSala());

        return db.update(TABLE, values, ID + " = ?",
                new String[]{String.valueOf(zajecie.getId())});
    }

    public void deleteZajecie(Zajecie zajecie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, ID + " = ?", new String[]{String.valueOf(zajecie.getId())});
        db.close();
    }

    public String zakresGodzin(int dzien) throws ParseException {

        StringBuilder zakres = new StringBuilder();

        String selectQueryMin = "SELECT MIN(" + GODZINA + ") FROM " + TABLE
                + " WHERE " + DZIEN + "=" + dzien;

        String selectQueryMax = "SELECT MAX(" + GODZINA + ") FROM " + TABLE
                + " WHERE " + DZIEN + "=" + dzien;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryMin, null);

        if (cursor.moveToFirst()) {

            String min = cursor.getString(0);
            if (min != null && !min.equals(""))
                zakres.append(min);

        }

        cursor.close();

        cursor = db.rawQuery(selectQueryMax, null);

        if (cursor.moveToFirst()) {

            String max = cursor.getString(0);
            if (max != null && !max.equals("")) {
                String max_plus_90 = dodajMinuty(max, 90);
                zakres.append(" - ").append(max_plus_90);
            }

        }

        cursor.close();

        return zakres.toString();

    }
}





