package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Connor Boyle on 2017-06-06.
 */

public class Notepad extends Fragment {

    EditText etNotepad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notepad_layout, container, false);
        etNotepad = (EditText) v.findViewById(R.id.etNotepad);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("allNotes", etNotepad.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) //TODO: Maybe move stuff to MainActivity
        etNotepad.setText(savedInstanceState.getString("allNotes"));
    }
}
