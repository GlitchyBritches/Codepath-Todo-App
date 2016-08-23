package com.charmedmatter.glitchybritches.todo_app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TodoAdapter extends CursorAdapter {


    public TodoAdapter(Context context, Cursor c,int flags) {
        super(context, c, flags);
        Log.i("TodoAdapter.java", "Cursor initialized");

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.i("TodoAdapter.java", "newView Firing");
        return LayoutInflater.from(context).inflate(R.layout.todo_item_priority, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Log.i("TodoAdapter.java", "bindView Firing");

        String todoItemText = cursor.getString(cursor.getColumnIndex(DatabaseHelperUtil.KEY_ITEM_NAME));
        int todoPriority = cursor.getInt(cursor.getColumnIndex(DatabaseHelperUtil.KEY_PRIORITY));
        int todoCompleteStatus = cursor.getInt(cursor.getColumnIndex(DatabaseHelperUtil.KEY_COMPLETE));

        TextView todoItem = (TextView) view.findViewById(R.id.txtTodoText);
        ImageView priorityItem = (ImageView) view.findViewById(R.id.imgPriority);

        todoItem.setText(todoItemText);
        if (todoCompleteStatus == 0)
        {
            todoItem.setTextColor(Color.BLACK);
            priorityItem.setImageResource(R.drawable.ic_record_w);
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
            //priorityItem.setBackgroundResource(R.drawable.ic_add_circle_black_48dp);
            todoItem.setTextColor(Color.GRAY);
            priorityItem.setImageResource(R.drawable.ic_done_black_48dp);
            priorityItem.setColorFilter(Color.GREEN);

        }







    }
}
