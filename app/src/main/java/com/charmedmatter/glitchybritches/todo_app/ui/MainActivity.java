package com.charmedmatter.glitchybritches.todo_app.ui;

import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.LoaderManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.charmedmatter.glitchybritches.todo_app.R;
import com.charmedmatter.glitchybritches.todo_app.ui.services.FragmentCommunicator;
import com.charmedmatter.glitchybritches.todo_app.ui.services.SectionCursorAdapter;
import com.charmedmatter.glitchybritches.todo_app.ui.services.TodoItemsAdapter;
import com.charmedmatter.glitchybritches.todo_app.data.TodoItemsContentProvider;
import com.charmedmatter.glitchybritches.todo_app.data.TodoItemsDbHelper;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, FragmentCommunicator<Object> {
    ListView lvItems;
    CursorAdapter todoItemsListAdapter;
    SectionCursorAdapter todoItemsAdapter;
    FragmentManager fragmentManager;
    public Spinner selectSpinner;

    private static final int LOADER_URI = 0;

    //Initialize adapter, click event listener, loader manager, and fragment manager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Setup action bar
        Toolbar listFreakToolbar = (Toolbar) findViewById(R.id.list_freak_toolbar);
        listFreakToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(listFreakToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        selectSpinner = (Spinner) findViewById(R.id.spinner_view);
        selectSpinner.setSelection(2);

        //Setup to-do list container
        lvItems = (ListView) findViewById(R.id.lvItems);
        todoItemsAdapter = new TodoItemsAdapter(this, null, 0);
        lvItems.setAdapter(todoItemsAdapter);

        //Setup click listeners, loaders, and fragment manager
        setupListeners();
        getLoaderManager().initLoader(LOADER_URI, null, this);
        fragmentManager = getSupportFragmentManager();

    }

    //Add new item via a dialog fragment
    protected void onAddItem(View v) {
        showDialog("newItem");
    }

    private void showDialog(String purpose) {
        if (purpose == "newItem") {
            fragmentManager = getSupportFragmentManager();
            EditTodoFragment newItemDialog = new EditTodoFragment();
            newItemDialog.show(fragmentManager, "TodoDialog");
        }

    }
    private void showDialog(String purpose, long id){
        if (purpose == "editItem") {
            Uri uri = Uri.parse(TodoItemsContentProvider.TABLE_URI + "/" + id);
            Cursor cursor = getContentResolver().query(uri, TodoItemsDbHelper.TODO_ITEMS_COLUMNS, null, null,null);
            cursor.moveToFirst();
            String itemName = cursor.getString(cursor.getColumnIndex(TodoItemsDbHelper.KEY_ITEM_NAME));
            int priority = cursor.getInt(cursor.getColumnIndex(TodoItemsDbHelper.KEY_PRIORITY));
            String dueDate = cursor.getString(cursor.getColumnIndex(TodoItemsDbHelper.KEY_DUE_DATE));
            String note = cursor.getString(cursor.getColumnIndex(TodoItemsDbHelper.KEY_NOTE));
            int complete = cursor.getInt(cursor.getColumnIndex(TodoItemsDbHelper.KEY_COMPLETE));
            cursor.close();

            EditTodoFragment editDialog = EditTodoFragment.newInstance(id, itemName, priority, dueDate, note, complete);
            editDialog.show(fragmentManager, "TodoDialog");

        }
    }

    // Click listeners (for long click (for deletions) and short clicks (for item edits))
    private void setupListeners() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {


                Uri uri = Uri.parse(TodoItemsContentProvider.TABLE_URI + "/" + id);
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
                        showDialog("editItem", id);

                    }
                });

        selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                restartLoader();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i("MainActivity", "No Spinner Position Selected");
            }
        });
    }

    //return user's ordering choice from spinner
    public int getItemOrder(){
        return selectSpinner.getSelectedItemPosition();
    }
    //Loader methods

    //Restarts loader when database is updated
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int loader_id, Bundle bundle) {

        //Define projection to be used based on user's choice from spinner
        String projection;
        int itemOrder = getItemOrder();
        switch (itemOrder){
            case 0:
                projection = TodoItemsDbHelper.SORT_BY_PRIORITY;

                break;
            case 1:
                projection = TodoItemsDbHelper.SORT_BY_DATE;
                break;
            default:
                projection = null;
                break;
        }

                return new CursorLoader(
                        this,   // Parent activity context
                        TodoItemsContentProvider.TABLE_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        projection       // Default sort order
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
            long idCurrent;
            idCurrent = values.getAsLong("_id");
            values.remove("_id");
            Uri uri = Uri.parse(TodoItemsContentProvider.TABLE_URI + "/" + idCurrent);
            getContentResolver().update(uri,values,null,null);
            restartLoader();
        }

        //Add new todoitem
        if (operation == "addNewTodoItem" && data instanceof ContentValues){
            ContentValues values = (ContentValues)data;
            Uri todoUri = getContentResolver().insert(TodoItemsContentProvider.TABLE_URI,
                    values);
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
            dateDialog.show(fragmentManager, "editDateDialog");
        }
    }
}

