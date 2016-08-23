package com.charmedmatter.glitchybritches.todo_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelperUtil extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    protected static final String TABLE_TODO = "todo_items";
    // Database Name
    private static final String DATABASE_NAME = "TODOItems.db";



    // Column names
    protected static final String KEY_ID = "_id";
    protected static final String KEY_ITEM_NAME = "item_name";
    protected static final String KEY_PRIORITY = "priority";
    protected static final String KEY_DUE_DATE = "due_date";
    protected static final String KEY_NOTE = "note";
    protected static final String KEY_COMPLETE = "complete";
    protected static final String KEY_CREATED_AT = "created_at";
    public static final String[] TODO_ITEMS_COLUMNS =
            {KEY_ID, KEY_ITEM_NAME, KEY_PRIORITY, KEY_DUE_DATE,
            KEY_NOTE, KEY_COMPLETE, KEY_CREATED_AT};



    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ITEM_NAME + " TEXT NOT NULL DEFAULT ''," + KEY_PRIORITY
            + " INT NOT NULL DEFAULT 0," + KEY_DUE_DATE + " DATE," + KEY_NOTE + " TEXT NOT NULL DEFAULT '',"
            + KEY_COMPLETE + " BOOLEAN NOT NULL DEFAULT 0,"
            + KEY_CREATED_AT + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" + ");";

    public DatabaseHelperUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("INFO","DatabaseHelperUtil.java - OnCreate Called");
        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

        // create new tables
        onCreate(db);
    }

    protected long addRowDirectly(String todo_item, int priority, String due_date, String notes, int complete) {
        //get timestamp
        SQLiteDatabase db = this.getWritableDatabase();

        //put content values in and insert
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, todo_item);
        values.put(KEY_PRIORITY, priority);
        values.put(KEY_DUE_DATE, due_date);
        values.put(KEY_NOTE, notes);
        if (complete > 1 | complete < 0) {
            complete = 0;
        }
        values.put(KEY_COMPLETE, complete);
        long result =  db.insert(TABLE_TODO, null, values);
        db.close();
        return result;

    }

    protected long addRowDirectly(String todo_item) {
        //get timestamp
        SQLiteDatabase db = this.getWritableDatabase();

        //put content values in and insert
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, todo_item);

        long result =  db.insert(TABLE_TODO, null, values);
        db.close();
        return result;

    }

    protected Cursor getAllRowsDirectly(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO + ");";
        Log.d(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        db.close();
        return c;
    }

    //delete entry
    protected void deleteRow(String todo_item, String date_created) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(todo_item, KEY_ITEM_NAME + " = ? AND " + KEY_CREATED_AT + " = ?" , new String[] {todo_item, date_created});
        db.close();

    }



}
