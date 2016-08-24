package com.charmedmatter.glitchybritches.todo_app;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.net.Uri;
import android.app.FragmentManager;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.LoaderManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, FragmentCommunicator<Object>{
    ListView lvItems;
    CursorAdapter todoItemsAdapter;
    FragmentManager fragmentManager;

    private static final int LOADER_URI = 0;

    //Initialize adapter & event listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        todoItemsAdapter = new TodoAdapter(this, null, 0);
        lvItems.setAdapter(todoItemsAdapter);
        setupListViewListener();

        getLoaderManager().initLoader(LOADER_URI, null, this);
        Log.i("INFO: MainActivity.java","onCreate() Fired");

    }

    //Add new item via adapter and then write to file
    public void onAddItem(View v) {
        showDialog("new_item");
    }

    private void showDialog(String purpose) {
        if (purpose == "new_item") {
            fragmentManager = getFragmentManager();
            EditTodoFragment newItemDialog = new EditTodoFragment();
            newItemDialog.show(fragmentManager, "TodoDialog");
        }

    }
    private void showDialog(String purpose, long id){
        if (purpose == "edit_item") {
            fragmentManager = getFragmentManager();

            Uri uri = Uri.parse(TodoContentProvider.TABLE_URI + "/" + id);
            Cursor cursor = getContentResolver().query(uri,DatabaseHelperUtil.TODO_ITEMS_COLUMNS, null, null,null);
            cursor.moveToFirst();
            String itemName = cursor.getString(cursor.getColumnIndex(DatabaseHelperUtil.KEY_ITEM_NAME));
            int priority = cursor.getInt(cursor.getColumnIndex(DatabaseHelperUtil.KEY_PRIORITY));
            String dueDate = cursor.getString(cursor.getColumnIndex(DatabaseHelperUtil.KEY_DUE_DATE));
            String note = cursor.getString(cursor.getColumnIndex(DatabaseHelperUtil.KEY_NOTE));
            int complete = cursor.getInt(cursor.getColumnIndex(DatabaseHelperUtil.KEY_COMPLETE));
            cursor.close();

            EditTodoFragment editDialog = EditTodoFragment.newInstance(id, itemName, priority, dueDate, note, complete);
            editDialog.show(fragmentManager, "TodoDialog");

        }
    }




    // Click listeners (for long click (for deletions) and short clicks (for item edits))
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {

                Log.i("INFO: MainActivity.java","onItemLongClickLister() Fired");
                Uri uri = Uri.parse(TodoContentProvider.TABLE_URI + "/" + id);
                getContentResolver().delete(uri, null, null);
                restartLoader();
                return true;
            }
        });


        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        Log.i("INFO: MainActivity.java","onItemClickLister() Fired");
                        showDialog("edit_item", id);

                    }
                });

    }

    //Loader methods

    //Restarts loader when database is updated
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loader_id, Bundle bundle) {
                return new CursorLoader(
                        this,   // Parent activity context
                        TodoContentProvider.TABLE_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        todoItemsAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoItemsAdapter.swapCursor(null);

    }

    //Interfaces for fragment communication
    /**
     * Sends data between Fragments
     * @param operation operation to be performed
     * @param data data to pass
    */
    public void sendData(String operation, Object data) {
        //Set date
        if (operation == "setTodoFragmentDate" && data instanceof String){
            EditTodoFragment editTodoFragment = (EditTodoFragment)fragmentManager.findFragmentByTag("TodoDialog");
            String date = (String)data;
            editTodoFragment.setDate(date);
        }

        //Store todoitem edit
        if (operation == "editTodoItem" && data instanceof ContentValues){
            ContentValues values = (ContentValues)data;
            long _id_current;
            _id_current = values.getAsLong("_id");
            values.remove("_id");
            Uri uri = Uri.parse(TodoContentProvider.TABLE_URI + "/" + _id_current);
            getContentResolver().update(uri,values,null,null);
            todoItemsAdapter.notifyDataSetChanged();
            restartLoader();
        }

        //Add new todoitem
        if (operation == "addNewTodoItem" && data instanceof ContentValues){
            ContentValues values = (ContentValues)data;
            Uri todoUri = getContentResolver().insert(TodoContentProvider.TABLE_URI,
                    values);
            todoItemsAdapter.notifyDataSetChanged();
            restartLoader();
        }

    }

    /**
     * Launch new fragment from existing fragment
     * @param fragmentClassName name of fragment class to launch
     */
    public void launchFragment(String fragmentClassName){
        if (fragmentClassName == "EditDateFragment"){
            EditDateFragment dateDialog = new EditDateFragment();
            Log.d("MainActivity.java",String.valueOf(dateDialog));
            dateDialog.show(fragmentManager, "editDateDialog");
        }
    }
}

