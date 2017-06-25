package com.connorboyle.elitetools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Connor Boyle on 2017-06-18.
 */

public class Notepad extends AppCompatActivity {

    EditText etTitle, etText;
    NoteEntry note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_layout);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etText = (EditText) findViewById(R.id.etNotepad);
//
//        etText.setOnKeyListener(this);
//        etTitle.setOnKeyListener(this);

        note = new NoteEntry("", "", System.currentTimeMillis());

        if (!getIntent().getBooleanExtra("newNote", true)) {
            note = (NoteEntry) getIntent().getSerializableExtra("noteToEdit");
            etText.setText(note.text);
            etTitle.setText(note.title);
        }
    }

//
//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        note.title = etTitle.getText().toString();
//        note.text = etText.getText().toString();
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        if (etTitle.getText().toString().trim().equals("") && etText.getText().toString().trim().equals("")) {
            setResult(RESULT_CANCELED);
        } else if (etTitle.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter a title for this note", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            note.title = etTitle.getText().toString();
            note.text = etText.getText().toString();
            note.modified = System.currentTimeMillis();
            setResult(RESULT_OK, new Intent().putExtra("savedNote", note));
        }
        finish();
        return true;
    }
}
