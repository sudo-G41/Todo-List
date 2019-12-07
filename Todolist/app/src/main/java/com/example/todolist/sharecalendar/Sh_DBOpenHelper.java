package com.example.todolist.sharecalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Sh_DBOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_EVENTS_TABLE = "create table " + Sh_DBStructure.EVENT_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Sh_DBStructure.EVENT + " TEXT," + Sh_DBStructure.TIME + " TEXT," + Sh_DBStructure.DATE + " TEXT," + Sh_DBStructure.MONTH + " TEXT,"
            + Sh_DBStructure.YEAR + " TEXT," + Sh_DBStructure.Notify + " TEXT)";
    private static final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS " + Sh_DBStructure.EVENT_TABLE_NAME;

    public Sh_DBOpenHelper(@Nullable Context context, int locale) {
        super(context, Sh_DBStructure.DB_NAME, null, locale);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        sqLiteDatabase.execSQL(DROP_EVENTS_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, String notify ,SQLiteDatabase datebase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Sh_DBStructure.EVENT, event);
        contentValues.put(Sh_DBStructure.TIME, time);
        contentValues.put(Sh_DBStructure.DATE, date);
        contentValues.put(Sh_DBStructure.MONTH, month);
        contentValues.put(Sh_DBStructure.YEAR, year);
        contentValues.put(Sh_DBStructure.Notify, notify);
        datebase.insert(Sh_DBStructure.EVENT_TABLE_NAME, null, contentValues);
    }


    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String[] Projections = {Sh_DBStructure.EVENT, Sh_DBStructure.TIME, Sh_DBStructure.DATE, Sh_DBStructure.MONTH, Sh_DBStructure.YEAR};
        String Selection = Sh_DBStructure.DATE + "=?";
        String SelectionArgs[] = {date};

        return database.query(Sh_DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);
    }

    public Cursor ReadIDEvents(String date, String event, String time, SQLiteDatabase database){
        String[] Projections = {Sh_DBStructure.ID, Sh_DBStructure.Notify};
        String Selection = Sh_DBStructure.DATE + "=? and " + Sh_DBStructure.EVENT + "=? and " + Sh_DBStructure.TIME + "=?";
        String SelectionArgs[] = {date, event, time};

        return database.query(Sh_DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String[] Projections = {Sh_DBStructure.EVENT, Sh_DBStructure.TIME, Sh_DBStructure.DATE, Sh_DBStructure.MONTH, Sh_DBStructure.YEAR};
        String Selection = Sh_DBStructure.MONTH + "=? and " + Sh_DBStructure.YEAR + "=?";
        String SelectionArgs[] = {month, year};

        return database.query(Sh_DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database) {
        String selection = Sh_DBStructure.EVENT + "=? and " + Sh_DBStructure.DATE + "=? and " + Sh_DBStructure.TIME + "=?";
        String[] selectionArgs = {event, date, time};
        database.delete(Sh_DBStructure.EVENT_TABLE_NAME, selection, selectionArgs);
    }

    public void updateEvent(String date, String event, String time, String notify, SQLiteDatabase database){
        ContentValues  contentValues = new ContentValues();
        contentValues.put(Sh_DBStructure.Notify, notify);
        String Selection = Sh_DBStructure.DATE + "=? and " + Sh_DBStructure.EVENT + "=? and " + Sh_DBStructure.TIME + "=?";
        String [] SelectionArgs = {date, event, time};
        database.update(Sh_DBStructure.EVENT_TABLE_NAME, contentValues, Selection, SelectionArgs);
    }
}
