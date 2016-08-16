package com.charmedmatter.glitchybritches.todo_app;

import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.LoaderManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    ListView lvItems;
    ArrayList<String> items;
    //ArrayAdapter<String> itemsAdapter;
    CursorAdapter todoItemsAdapter;


    static final int EDIT_LIST_ITEM = 7734;

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

        ///Code about to be deprecated
        //readItems();
        //itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);


    }

    //Add new item via adapter and then write to file
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        etNewItem.setText("");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelperUtil.KEY_ITEM_NAME, itemText);
        values.put(DatabaseHelperUtil.KEY_PRIORITY, 1);
        Uri noteUri = getContentResolver().insert(TodoContentProvider.TABLE_URI,
                values);
        //todoItemsAdapter.notifyDataSetChanged();
        restartLoader();
    }

    //Restarts loader when database is updated
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    //Launch edit activity via an intent (with result when item is clicked (via anonymous
    //event listener) pass the text & list position of the item via the intent.
    private void launchActivity(int pos) {
        Intent q = new Intent(this, edititem.class);
        String text = items.get(pos).toString();
        Log.d("INFO",text);

        q.putExtra("editTextString", text);
        q.putExtra("pos", pos);
        startActivityForResult(q, EDIT_LIST_ITEM);
    }

    //TODO: Add in support for editing via an activity or fragment
    //Receive edited text from edit activity when it terminates. Modify list item
    //with changes user made in edit activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_LIST_ITEM) {
            //int pos = data.getExtras().getInt("pos");
            //String newText = data.getExtras().getString("editText");
            //items.set(pos, newText);
            //itemsAdapter.notifyDataSetChanged();
            //writeItems();
        } else {
            Log.w("WARN","onActivityResult from edititem activity failed");
        }
    }

    // Setup two anonymous click listeners for long click (for deletions)
    // and short clicks (for item edits)
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {
                //items.remove(pos);
                //itemsAdapter.notifyDataSetChanged();
                //writeItems();
                Log.i("INFO: MainActivity.java","onItemLongClickLister() Fired");
                return true;
            }
        });


        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        //launchActivity(pos);
                        Log.i("INFO: MainActivity.java","onItemClickLister() Fired");
                    }
                });

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


}

