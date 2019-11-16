package com.example.todolist.ShareList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE = "create table" + DBStructure.EVENT_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBStructure.EVENT + " TEXT," + DBStructure.TIME+ " TEXT," + DBStructure.DATE + " TEXT," + DBStructure.MONTH + " TEXT,"
            + DBStructure.YEAR + " TEXT,)";
    private static final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS" + DBStructure.EVENT_TABLE_NAME;

    public DBOpenHelper(Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_EVENTS_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase datebase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT, event);
        contentValues.put(DBStructure.TIME, time);
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        datebase.insert(DBStructure.EVENT_TABLE_NAME, null, contentValues);
    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String[] Projections = {DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR};
        String Selection = DBStructure.DATE + "=?";
        String SelectionArgs[] = {date};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);

    }
    public Cursor ReadEventsperMonth(String date, SQLiteDatabase database){
        String[] Projections = {DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR};
        String Selection = DBStructure.MONTH + "=? and" + DBStructure.YEAR + "=?";
        String SelectionArgs[] = {date};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);

    }
}
