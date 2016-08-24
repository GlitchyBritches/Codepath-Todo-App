package com.charmedmatter.glitchybritches.todo_app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;


public class TodoContentProvider extends ContentProvider {

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
        DatabaseHelperUtil dbUtil =  new DatabaseHelperUtil(getContext());
        db = dbUtil.getWritableDatabase();
        return false;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        Log.i("TodoContentProvider", uri.toString());
        Log.i("TodoContentProvider", String.valueOf(uriMatcher.match(uri)));

        if (uriMatcher.match(uri) == TODOITEMSTABLE_ROWWITHKEY){
            selection = DatabaseHelperUtil.KEY_ID + "=" + uri.getLastPathSegment();
            selectionArgs = null;
            Log.i("TodoContentProvider", selection);
        }


        return db.query(DatabaseHelperUtil.TABLE_TODO, columns,
                selection, selectionArgs,null, null, DatabaseHelperUtil.KEY_CREATED_AT + " DESC");

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = db.insert(DatabaseHelperUtil.TABLE_TODO, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == TODOITEMSTABLE_ROWWITHKEY){
            selection = DatabaseHelperUtil.KEY_ID + "=" + uri.getLastPathSegment();
            selectionArgs = null;
         }
        return db.delete(DatabaseHelperUtil.TABLE_TODO, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == TODOITEMSTABLE_ROWWITHKEY){
            selection = DatabaseHelperUtil.KEY_ID + "=" + uri.getLastPathSegment();
            selectionArgs = null;
            Log.i("TodoContentProvider", selection);
        }
        return db.update(DatabaseHelperUtil.TABLE_TODO,contentValues,selection, selectionArgs);
    }
}
