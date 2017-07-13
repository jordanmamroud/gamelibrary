package com.example.jordan.trivialibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Jordan on 7/11/2017.
 */

public class SQLSaver extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dinosaurtrivia";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "entry";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_TITLE = "dinosaur_name";
    public static final String COLUMN_NAME_POINTS = "dinosaur_points";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE  + " TEXT, " +
                    COLUMN_NAME_POINTS + " TEXT)";

    public SQLSaver(Context context) {
        super(context, DATABASE_NAME, null  , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( SQL_CREATE_ENTRIES   );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +    TABLE_NAME  );
        onCreate(db);
    }

    public void addItem(String name , String points){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TITLE, name);
        contentValues.put(COLUMN_NAME_POINTS, points);

        db.insert(TABLE_NAME, null, contentValues);
    }

    public boolean updateItem(Integer id, String name, String points) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TITLE, name);
        contentValues.put(COLUMN_NAME_POINTS, points);

        db.update(TABLE_NAME, contentValues, _ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public ArrayList<String> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
        ArrayList<String> allcompleted= new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                // dinosaur name is in column 1;
               allcompleted.add(    cursor.getString(1) );

            } while (cursor.moveToNext());
        }

        cursor.close();
        return allcompleted;
    }
}
