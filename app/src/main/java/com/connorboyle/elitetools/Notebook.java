package com.connorboyle.elitetools;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Connor Boyle on 2017-06-06.
 */

public class Notebook extends Fragment implements View.OnClickListener, ListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    View view;
    ListView lvNotebook;
    Button btnNewNote;

    static final int ADD_NEW_NOTE = 1;
    static final int UPDATE_NOTE = 2;

    NoteDBHelper db;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.notebook_layout, container, false);
            lvNotebook = (ListView) view.findViewById(R.id.lvNotebook);
            lvNotebook.setOnItemClickListener(this);
            lvNotebook.setOnItemLongClickListener(this);

            btnNewNote = (Button) view.findViewById(R.id.btnNewNote);
            btnNewNote.setOnClickListener(this);
        }


        db = new NoteDBHelper(getContext());

        refreshList();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewNote) {
            Intent i = new Intent(this.getActivity(), Notepad.class)
                    .putExtra("newNote", true);
            startActivityForResult(i, ADD_NEW_NOTE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cursor.moveToPosition(position);
        NoteEntry note = new NoteEntry(
                cursor.getLong(0), cursor.getString(1), cursor.getString(2), System.currentTimeMillis());
        Intent i = new Intent(this.getActivity(), Notepad.class)
                .putExtra("newNote", false)
                .putExtra("noteToEdit", note);
        startActivityForResult(i, UPDATE_NOTE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        cursor.moveToPosition(position);
        NoteEntry noteToDelete = new NoteEntry(
                cursor.getLong(0), cursor.getString(1), cursor.getString(2), 0);
        db.open();
        db.deleteNote(noteToDelete);
        db.close();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        NoteEntry note = (NoteEntry) data.getSerializableExtra("note");
        if (resultCode == RESULT_OK)
        switch (requestCode) {
            case ADD_NEW_NOTE:
                db.open();
                db.createNote(note);
                db.close();
                break;
            case UPDATE_NOTE:
                db.open();
                db.updateNote(note);
                db.close();
                break;
        }
    }

    private void refreshList() {
        db.open();
        cursor = db.getNotes();
        if (cursor.moveToFirst()) {
            SimpleCursorAdapter sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, cursor, new String [] {"title"}, null);
            lvNotebook.setAdapter(sca);
        }
        db.close();
    }
}
