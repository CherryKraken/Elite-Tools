package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by Connor Boyle on 2017-06-18.
 */

public class Notepad extends AppCompatActivity {

    EditText etTitle, etText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_layout);
    }
}
