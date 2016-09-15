package com.charmedmatter.glitchybritches.todo_app.ui.services;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charmedmatter.glitchybritches.todo_app.R;
import com.charmedmatter.glitchybritches.todo_app.data.TodoItemsDbHelper;
import com.charmedmatter.glitchybritches.todo_app.ui.MainActivity;
import com.charmedmatter.glitchybritches.todo_app.utils.TimeDateHelper;

import java.util.SortedMap;
import java.util.TreeMap;

//Extension of SectionCursorAdapter by twotoasters
public class TodoItemsAdapter extends SectionCursorAdapter {

    Context appContext;

    public TodoItemsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        appContext = context;
    }

    protected TodoItemsAdapter(Context context, Cursor c, boolean autoRequery, SortedMap<Integer, Object> sections) {
        super(context, c, autoRequery, sections);
    }


    public TodoItemsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    protected Object getSectionFromCursor(Cursor cursor) {
        return null;
    }

    //Logic for creation of descriptive custom headers
    @Override
    protected SortedMap<Integer, Object> buildSections(Cursor cursor) {
        TreeMap<Integer, Object> sections = new TreeMap<>();

        //Get ordering preference from spinner
        int itemOrder = ((MainActivity)mContext).getItemOrder();
        int columnIndex;
        int cursorPosition = 0;

        switch (itemOrder){
            //Case: by priority
            case 0:
                columnIndex = cursor.getColumnIndex(TodoItemsDbHelper.KEY_PRIORITY);
                String[] priorities = appContext.getResources().getStringArray(R.array.priorities);

                while (cursor.moveToNext()) {
                    int priority = cursor.getInt(columnIndex);
                    if (!sections.containsValue(priorities[priority])) {
                        sections.put(cursorPosition + sections.size(), priorities[priority]);
                    }
                    cursorPosition++;
                }
                break;
            //Case: by due date
            case 1:
                columnIndex = cursor.getColumnIndex(TodoItemsDbHelper.KEY_DUE_DATE);
                String date;
                String dateHeader;


                while (cursor.moveToNext()) {
                    date = cursor.getString(columnIndex);
                    dateHeader = TimeDateHelper.dueStatus(appContext, date);
                    if (!sections.containsValue(dateHeader)) {
                        sections.put(cursorPosition + sections.size(), dateHeader);
                    }
                    cursorPosition++;
                }

                break;
            default:
                break;
        }

        return sections;
    }

    @Override
    protected View newSectionView(Context context, Object item, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.todo_header, parent, false);
    }

    //Set header text & set color to red if "Urgent" in priority view or "Overdue" in
    //due date view
    @Override
    protected void bindSectionView(View convertView, Context context, int position, Object item) {
        TextView header = (TextView) convertView.findViewById(R.id.txtViewSectionHeader);
        String headerText = (String) mSections.get(position);
        header.setText(headerText);
        if (header.getText().equals("Overdue") | header.getText().equals("Urgent")) {
            header.setBackgroundColor(ContextCompat.getColor(mContext, R.color.listfreak_urgent));
        }
        else {
            header.setBackgroundColor(ContextCompat.getColor(mContext, R.color.listfreak_color));
        }
    }

    @Override
    protected View newItemView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.todo_item_priority, parent, false);
    }

    //Provide all list items with text & priority color
    @Override
    protected void bindItemView(View convertView, Context context, Cursor cursor) {

        String todoItemText = cursor.getString(cursor.getColumnIndex(TodoItemsDbHelper.KEY_ITEM_NAME));
        int todoPriority = cursor.getInt(cursor.getColumnIndex(TodoItemsDbHelper.KEY_PRIORITY));
        int todoCompleteStatus = cursor.getInt(cursor.getColumnIndex(TodoItemsDbHelper.KEY_COMPLETE));

        TextView todoItem = (TextView) convertView.findViewById(R.id.txtTodoText);
        ImageView priorityItem = (ImageView) convertView.findViewById(R.id.imgPriority);

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
