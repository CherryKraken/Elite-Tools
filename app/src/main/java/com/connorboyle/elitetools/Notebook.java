package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Connor Boyle on 2017-06-06.
 */

public class Notebook extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {

    View view;
    ListView lvNotebook;
    Button btnNewNote;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.notebook_layout, container, false);
            lvNotebook = (ListView) view.findViewById(R.id.lvNotebook);
            btnNewNote = (Button) view.findViewById(R.id.btnNewNote);
        }



        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewNote) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
