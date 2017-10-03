package com.connorboyle.elitetools.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetEngineersTask;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 20-Sep-17.
 */

public class EngineersActivity extends Fragment {
    private View v;

    private Spinner selEngineers;

    private ArrayList<String> engrNames, engrIDs;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.engineers_layout, container, false);
        setupControls();
        new GetEngineersTask(this).execute();
        return v;
    }

    private void setupControls() {
        selEngineers = (Spinner) v.findViewById(R.id.selEngineers);
    }

    public void onEngineersTaskCompleted(ArrayList<String> strings) {
        engrIDs = new ArrayList<>();
        engrNames = new ArrayList<>();
        int i = 0;

        while (i < strings.size()) {
            engrIDs.add(strings.get(i++));
            engrNames.add(strings.get(i++));
        }

        selEngineers.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, engrNames));

    }
}
