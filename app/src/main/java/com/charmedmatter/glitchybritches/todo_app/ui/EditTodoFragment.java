package com.charmedmatter.glitchybritches.todo_app.ui;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.charmedmatter.glitchybritches.todo_app.R;
import com.charmedmatter.glitchybritches.todo_app.data.TodoItemsDbHelper;
import com.charmedmatter.glitchybritches.todo_app.ui.services.FragmentCommunicator;


public class EditTodoFragment extends DialogFragment implements View.OnClickListener {

    //Fragment communicator instance + other view items that need to be populated
    FragmentCommunicator fragmentCommunicator;
    long _id;
    EditText todoItemNameField;
    Spinner prioritySpinner;
    EditText dateField;
    EditText notesField;
    CheckBox completedCheckBox;
    Button saveButton;
    ImageView closeDialog;

    //Empty constructor + constructor for passing data
    public EditTodoFragment() {

    }

    public static EditTodoFragment newInstance(long id, String itemName, int priority, String dueDate, String notes, int complete) {
        EditTodoFragment frag = new EditTodoFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putString("itemName", itemName);
        args.putInt("priority", priority);
        args.putString("dueDate", dueDate);
        args.putString("notes", notes);
        args.putInt("complete", complete);
        frag.setArguments(args);
        return frag;
    }

    //Overriden methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_fragment, null);
        dateField = (EditText)v.findViewById(R.id.editDueDate);
        closeDialog = (ImageView)v.findViewById(R.id.closeTodoDialog);
        todoItemNameField = (EditText) v.findViewById(R.id.editItemName);
        notesField = (EditText) v.findViewById(R.id.editNotes);
        prioritySpinner = (Spinner) v.findViewById(R.id.spinner_priority_level);
        completedCheckBox = (CheckBox) v.findViewById(R.id.checkComplete);
        saveButton = (Button)v.findViewById(R.id.btnSave);

        closeDialog.setOnClickListener(this);
        dateField.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        if (getArguments() != null) {

        populateFields(getArguments().getLong("id"),getArguments().getString("itemName"),
                    getArguments().getInt("priority"),getArguments().getString("dueDate"),
                    getArguments().getString("notes"),getArguments().getInt("complete"));
        }

        return v;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCommunicator=(FragmentCommunicator)context;

    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
    }

    //Custom implementation of onClick method to send data back to main activity for insertion into
    //the database
    public void onClick(View v) {

        if (v.getId() == R.id.editDueDate){
            fragmentCommunicator.launchFragment("EditDateFragment");
        }

        if (v.getId() == R.id.closeTodoDialog){
            dismiss();
        }

        if (v.getId() == R.id.btnSave) {

            ContentValues todoItem = new ContentValues();
            String todoItemNameText = todoItemNameField.getText().toString();
            String notesText = notesField.getText().toString();
            String priorityText = prioritySpinner.getSelectedItem().toString();
            String dueDateText = dateField.getText().toString();
            int complete;
            if (completedCheckBox.isChecked()) {
                complete = 1;
            } else {
                complete = 0;
            }

            int priority;
            switch (priorityText) {
                case "Low":
                    priority = 0;
                    break;
                case "Medium":
                    priority = 1;
                    break;
                case "High":
                    priority = 2;
                    break;
                case "Urgent":
                    priority = 3;
                    break;
                default:
                    priority = 0;
                    break;
            }

            todoItem.put(TodoItemsDbHelper.KEY_ITEM_NAME, todoItemNameText);
            todoItem.put(TodoItemsDbHelper.KEY_PRIORITY, priority);
            todoItem.put(TodoItemsDbHelper.KEY_DUE_DATE, dueDateText);
            todoItem.put(TodoItemsDbHelper.KEY_NOTE, notesText);
            todoItem.put(TodoItemsDbHelper.KEY_COMPLETE, complete);

            if (getArguments() == null) {
                fragmentCommunicator.sendData("addNewTodoItem", todoItem);
            }
            if (getArguments() != null) {
                todoItem.put(TodoItemsDbHelper.KEY_ID, _id);
                fragmentCommunicator.sendData("editTodoItem", todoItem);
            }

            dismiss();
        }
    }

    //Custom utility methods for setting the date & populating fields from existing items
    protected void setDate(String date){
        dateField.setText(date);
    }

    private void populateFields(long id, String itemName, int priority, String dueDate, String notes, int complete){
        _id = id;
        todoItemNameField.setText(itemName);
        prioritySpinner.setSelection(priority);
        dateField.setText(dueDate);
        notesField.setText(notes);
        if (complete == 1) {
            completedCheckBox.setChecked(true);
        }

    }


}
