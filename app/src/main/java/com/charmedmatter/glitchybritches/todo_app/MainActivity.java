package com.charmedmatter.glitchybritches.todo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//The XML somehow makes the object
//in the code we find it by the android R object and assign a reference to it via
//lvItems in code (I think) and that allows us to manipulate it



public class MainActivity extends AppCompatActivity {
    ListView lvItems;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    static final int EDIT_LIST_ITEM = 7734;


    //Initialize adapter & event listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

    }

    //Add new item via adapter and then write to file
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
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

    //Receive edited text from edit activity when it terminates. Modify list item
    //with changes user made in edit activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_LIST_ITEM) {
            int pos = data.getExtras().getInt("pos");
            String newText = data.getExtras().getString("editText");
            items.set(pos, newText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        } else {
            Log.d("WARN","onActivityResult from edititem activity failed");
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
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });


        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        launchActivity(pos);
                    }
                });

    }

    //Read items into memory from file on device
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }

    }

    //Write items to file on device
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

