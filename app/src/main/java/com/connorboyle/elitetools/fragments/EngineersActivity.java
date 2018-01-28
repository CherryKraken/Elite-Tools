package com.connorboyle.elitetools.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetEngineerDetailTask;
import com.connorboyle.elitetools.asynctasks.GetEngineersTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;
import com.connorboyle.elitetools.models.Engineer;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 20-Sep-17.
 */

public class EngineersActivity extends Fragment implements OnTaskCompleteHelper {
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

    @Override
    public void onAsyncTaskComplete(Task task, Object obj) {
        switch (task) {
            case ENGINEERS:engrIDs = new ArrayList<>();
                ArrayList<String> strings = (ArrayList<String>) obj;
                engrNames = new ArrayList<>();
                int i = 0;

                while (i < strings.size()) {
                    engrIDs.add(strings.get(i++));
                    engrNames.add(strings.get(i++));
                }

                selEngineers.setAdapter(new ArrayAdapter<>(
                        getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, engrNames));
                selEngineers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        new GetEngineerDetailTask(EngineersActivity.this).execute(engrIDs.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
                break;
            case ENGINEER_INFO:
                Engineer engineer = (Engineer) obj;
                ((TextView) v.findViewById(R.id.tvEngrName)).setText(engineer.name);
                ((TextView) v.findViewById(R.id.tvEngrSystem)).setText(engineer.system);
                ((TextView) v.findViewById(R.id.tvDReq)).setText(engineer.discover_req);
                ((TextView) v.findViewById(R.id.tvMReq)).setText(engineer.meeting_req);
                ((TextView) v.findViewById(R.id.tvUReq)).setText(engineer.unlock_req);
                break;
        }
    }
}
