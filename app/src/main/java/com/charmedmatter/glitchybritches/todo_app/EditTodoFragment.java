package com.charmedmatter.glitchybritches.todo_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
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
import android.widget.TextView;


public class EditTodoFragment extends DialogFragment implements View.OnClickListener {

    FragmentCommunicator fragmentCommunicator;
    long _id;
    EditText todoItemNameField;
    Spinner prioritySpinner;
    EditText dateField;
    EditText notesField;
    CheckBox completedCheckBox;
    Button saveButton;
    ImageView closeDialog;

    public EditTodoFragment() {

    }


    public static EditTodoFragment newInstance(long id, String item_name, int priority, String due_date, String notes, int complete) {
        EditTodoFragment frag = new EditTodoFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putString("item_name", item_name);
        args.putInt("priority", priority);
        args.putString("due_date", due_date);
        args.putString("notes", notes);
        args.putInt("complete", complete);
        frag.setArguments(args);
        return frag;
    }





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

        /**
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentCommunicator.launchFragment("EditDateFragment");
            }

        });
        **/

        closeDialog.setOnClickListener(this);
        dateField.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        if (getArguments() != null) {

        populateFields(getArguments().getLong("id"),getArguments().getString("item_name"),
                    getArguments().getInt("priority"),getArguments().getString("due_date"),
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

        /**
        View currentView = getView();
        todoItemNameField = (EditText) currentView.findViewById(R.id.editItemName);
        notesField = (EditText) currentView.findViewById(R.id.editNotes);
        prioritySpinner = (Spinner) currentView.findViewById(R.id.spinner_priority_level);
        completedCheckBox = (CheckBox) currentView.findViewById(R.id.checkComplete);
        **/

        Window window = getDialog().getWindow();
        //window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 220);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }

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


            todoItem.put(DatabaseHelperUtil.KEY_ITEM_NAME, todoItemNameText);
            todoItem.put(DatabaseHelperUtil.KEY_PRIORITY, priority);
            todoItem.put(DatabaseHelperUtil.KEY_DUE_DATE, dueDateText);
            todoItem.put(DatabaseHelperUtil.KEY_NOTE, notesText);
            todoItem.put(DatabaseHelperUtil.KEY_COMPLETE, complete);

            Log.i("EditTodoFragment.java", this.getTag());
            if (getArguments() == null) {
                Log.i("EditTodoFragment.java", "newtododialog firing");
                fragmentCommunicator.sendData("addNewTodoItem", todoItem);
            }
            if (getArguments() != null) {
                todoItem.put(DatabaseHelperUtil.KEY_ID, _id);
                fragmentCommunicator.sendData("editTodoItem", todoItem);
            }

            dismiss();
        }
    }

    protected void setDate(String date){
        dateField.setText(date);
    }

    private void populateFields(long id, String item_name, int priority, String due_date, String notes, int complete){
        _id = id;
        todoItemNameField.setText(item_name);
        prioritySpinner.setSelection(priority);
        dateField.setText(due_date);
        notesField.setText(notes);
        if (complete == 1) {
            completedCheckBox.setChecked(true);
        }

    }


}
