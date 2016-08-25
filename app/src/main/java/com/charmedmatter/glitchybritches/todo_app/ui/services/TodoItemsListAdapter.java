package com.charmedmatter.glitchybritches.todo_app.ui.services;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charmedmatter.glitchybritches.todo_app.R;
import com.charmedmatter.glitchybritches.todo_app.data.TodoItemsDbHelper;


public class TodoItemsListAdapter extends CursorAdapter {


    public TodoItemsListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.todo_item_priority, viewGroup, false);
    }

    //Bindview changes color based upon priority and icon based on completion status
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String todoItemText = cursor.getString(cursor.getColumnIndex(TodoItemsDbHelper.KEY_ITEM_NAME));
        int todoPriority = cursor.getInt(cursor.getColumnIndex(TodoItemsDbHelper.KEY_PRIORITY));
        int todoCompleteStatus = cursor.getInt(cursor.getColumnIndex(TodoItemsDbHelper.KEY_COMPLETE));

        TextView todoItem = (TextView) view.findViewById(R.id.txtTodoText);
        ImageView priorityItem = (ImageView) view.findViewById(R.id.imgPriority);

        todoItem.setText(todoItemText);
        if (todoCompleteStatus == 0)
        {
            todoItem.setTextColor(Color.BLACK);

            priorityItem.setImageResource(R.drawable.listfreak_btn_radio_on_holo_light);
            if (todoPriority == 0) {
                priorityItem.setColorFilter(android.graphics.Color.GREEN);

            } else if (todoPriority == 1) {
                priorityItem.setColorFilter(android.graphics.Color.YELLOW);

            } else if (todoPriority == 2) {
                priorityItem.setColorFilter(android.graphics.Color.RED);

            } else if (todoPriority == 3) {
                priorityItem.setColorFilter(android.graphics.Color.MAGENTA);
            } else {
                priorityItem.setColorFilter(android.graphics.Color.GREEN);
            }
        } else {
            todoItem.setTextColor(Color.GRAY);
            priorityItem.setImageResource(R.drawable.ic_done_black_48dp);
            priorityItem.setColorFilter(Color.GREEN);

        }







    }
}
