package com.warranty.tracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.warranty.tracker.modal.WarrantyItem;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "warrantydb";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "mywarrantys";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "name";

    private static final String DATE_COL = "date";

    private static final String IMAGE_COL = "image";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + IMAGE_COL + " TEXT)";

        db.execSQL(query);
    }

    public ArrayList<WarrantyItem> getAllWarrantyList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<WarrantyItem> courseModalArrayList = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                WarrantyItem warrantyItem = new WarrantyItem();
                warrantyItem.setId(cursorCourses.getInt(0));
                warrantyItem.setName(cursorCourses.getString(1));
                warrantyItem.setDate(cursorCourses.getString(2));
                warrantyItem.setImagePath(cursorCourses.getString(3));
                courseModalArrayList.add(warrantyItem);
            } while (cursorCourses.moveToNext());
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return courseModalArrayList;
    }

    public WarrantyItem getAllWarrantyItemById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE id='"+id+"'", null);
        WarrantyItem warrantyItem = new WarrantyItem();
        if (cursorCourses.moveToFirst()) {
            warrantyItem.setId(cursorCourses.getInt(0));
            warrantyItem.setName(cursorCourses.getString(1));
            warrantyItem.setDate(cursorCourses.getString(2));
            warrantyItem.setImagePath(cursorCourses.getString(3));
        }
        cursorCourses.close();
        return warrantyItem;
    }

    public void addNewWarranty(String warrantyName, String warrantyDate, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, warrantyName);
        values.put(DATE_COL, warrantyDate);
        values.put(IMAGE_COL, imagePath);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
