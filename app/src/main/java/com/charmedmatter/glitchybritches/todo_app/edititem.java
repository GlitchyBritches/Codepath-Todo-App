package com.charmedmatter.glitchybritches.todo_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class edititem extends AppCompatActivity {


    EditText etEditItem;
    int pos;

    //Get data from item that was clicked, fill EditText field from it
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edititem);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String editableString = getIntent().getExtras().getString("editTextString");
        pos = getIntent().getExtras().getInt("pos");
        etEditItem.setText(editableString);

    }

    //When save button is pressed, send modified data back to onActivityResult
    //method in MainActivity
    protected void onSave(View v) {
        String editedText = etEditItem.getText().toString();
        Intent intent = this.getIntent();
        intent.putExtra("editText", editedText);
        intent.putExtra("pos", pos);
        this.setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
