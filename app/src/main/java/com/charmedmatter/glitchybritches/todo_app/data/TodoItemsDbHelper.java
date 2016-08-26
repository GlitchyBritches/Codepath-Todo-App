package com.charmedmatter.glitchybritches.todo_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoItemsDbHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    protected static final String TABLE_TODO = "todo_items";
    // Database Name
    private static final String DATABASE_NAME = "TODOItems.db";



    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_DUE_DATE = "due_date";
    public static final String KEY_NOTE = "note";
    public static final String KEY_COMPLETE = "complete";
    public static final String KEY_CREATED_AT = "created_at";
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

    public TodoItemsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

}
