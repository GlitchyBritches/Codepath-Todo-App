package com.charmedmatter.glitchybritches.todo_app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class TodoItemsContentProvider extends ContentProvider {

    //Declare data source
    SQLiteDatabase db;

    //Declare all content resources we need
    private static final String AUTHORITY = "com.charmedmatter.glitchybritches.todo_app.todocontentprovider";
    private static final String BASE_PATH = "todo_items";
    public static final Uri TABLE_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static final int TODOITEMSTABLE = 1;
    private static final int TODOITEMSTABLE_ROW = 2;
    private static final int TODOITEMSTABLE_ROWWITHKEY = 3;

    //Define a URI matcher for determining which content resource type we want
    private  static  final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TODOITEMSTABLE);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODOITEMSTABLE_ROWWITHKEY);
    }



    @Override
    public boolean onCreate() {
        TodoItemsDbHelper dbUtil =  new TodoItemsDbHelper(getContext());
        db = dbUtil.getWritableDatabase();
        return false;
    }


    //Encapsulated content provider implementations for insert, delete, update and query functions
    //to the local sqllite database
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == TODOITEMSTABLE_ROWWITHKEY){
            selection = TodoItemsDbHelper.KEY_ID + "=" + uri.getLastPathSegment();
            selectionArgs = null;
        }


        return db.query(TodoItemsDbHelper.TABLE_TODO, columns,
                selection, selectionArgs,null, null, TodoItemsDbHelper.KEY_CREATED_AT + " DESC");

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = db.insert(TodoItemsDbHelper.TABLE_TODO, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == TODOITEMSTABLE_ROWWITHKEY){
            selection = TodoItemsDbHelper.KEY_ID + "=" + uri.getLastPathSegment();
            selectionArgs = null;
         }
        return db.delete(TodoItemsDbHelper.TABLE_TODO, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == TODOITEMSTABLE_ROWWITHKEY){
            selection = TodoItemsDbHelper.KEY_ID + "=" + uri.getLastPathSegment();
            selectionArgs = null;
        }
        return db.update(TodoItemsDbHelper.TABLE_TODO,contentValues,selection, selectionArgs);
    }
}
