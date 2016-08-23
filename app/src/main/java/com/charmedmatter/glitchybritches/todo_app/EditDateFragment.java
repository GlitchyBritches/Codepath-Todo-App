package com.charmedmatter.glitchybritches.todo_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

public class EditDateFragment extends DialogFragment implements View.OnClickListener {


    Button setDate;
    FragmentCommunicator fragmentCommunicator;
    DatePicker picker;

    public EditDateFragment() {

    }

    public static EditDateFragment newInstance(String date) {
        EditDateFragment frag = new EditDateFragment();
        Bundle args = new Bundle();
        args.putString("item_name", date);
        return frag;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_fragment_date_picker,null);
        setDate = (Button)v.findViewById(R.id.button2);
        setDate.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCommunicator=(FragmentCommunicator)context;

    }


    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        //window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 220);
        window.setGravity(Gravity.CENTER);
        //TODO: Set methods for restoring state in case of app interruption or screen rotation
    }

    //Process date integers from DatePicker and transform them into sqlite3 ready string
    private String processDateString(int day, int month, int year){
        String monthString;
        String dayString;

        if (month < 10) {
            monthString = "0" + String.valueOf(month);
        }
        else {
            monthString = String.valueOf(month);
        }

        if (day < 10) {
            dayString = "0" + String.valueOf(day);
        }
        else {
            dayString = String.valueOf(day);
        }

        String date = String.valueOf(year) + "-" + monthString + "-" + dayString;

        return date;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            DatePicker datePicker = (DatePicker) getView().findViewById(R.id.datePicker);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String dateString = processDateString(day, month, year);

            Log.i("EditDateFragment.java",dateString);

            dismiss();

            fragmentCommunicator.sendData("setTodoFragmentDate", dateString);
        }

    }
}