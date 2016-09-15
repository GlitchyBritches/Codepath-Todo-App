package com.charmedmatter.glitchybritches.todo_app.utils;

import android.content.Context;

import com.charmedmatter.glitchybritches.todo_app.R;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.text.ParseException;

//Utility class that provides helper methods for common app tasks involving dates & times
public class TimeDateHelper {

    LocalDate today = LocalDate.now();
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    LocalDate nextWeek = LocalDate.now().withDayOfWeek(DateTimeConstants.SATURDAY).plusDays(1);

    //Examine due date and return string indicating status
    public static String dueStatus(Context context, String date) {
        String dateCategory = "No Date";
        SimpleDateFormat formatTest = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate inputDate;
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate nextWeek = LocalDate.now().withDayOfWeek(DateTimeConstants.SATURDAY).plusDays(1);
        String dateHeader;

        //Use simpledate format to ensure date is in correct format
        try {
            formatTest.parse(date);
            inputDate = LocalDate.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
            if (inputDate.isBefore(today)) {
                dateCategory = context.getString(R.string.overdue);
            } else if (inputDate.equals(today)){
                dateCategory = context.getString(R.string.due_today);
            } else if (inputDate.equals(tomorrow)) {
                dateCategory = context.getString(R.string.due_tommorrow);
            } else if (inputDate.isAfter(tomorrow) && inputDate.isBefore(nextWeek)){
                dateCategory = context.getString(R.string.due_this_week);
            } else {
                dateCategory = context.getString(R.string.due_after_this_week);
            }
        }
        catch(ParseException e){
        }
        return dateCategory;
    }


}
