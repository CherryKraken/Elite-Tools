package com.connorboyle.elitetools.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetSystemInfoTask;
import com.connorboyle.elitetools.asynctasks.GetSystemsLiteTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;
import com.connorboyle.elitetools.classes.System;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 22-Oct-17.
 */

public class SystemFinder extends Fragment implements OnTaskCompleteHelper, View.OnClickListener {

    private View v;
    private AutoCompleteTextView etSystemToFind;
    private Spinner selSystemAllegiance, selSystemGov, selSystemEconomy, selSystemState, selMaxDistance;
    private Button btnSystem, btnFilterSystems;

    ArrayList<System> sysResults;
    private ListView lvFilteredSystems;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.system_finder_layout, container, false);
        setupControls();

        new GetSystemsLiteTask(this).execute();
        return v;
    }

    private void setupControls() {
        etSystemToFind = (AutoCompleteTextView) v.findViewById(R.id.etSystemToFind);
        selSystemAllegiance = (Spinner) v.findViewById(R.id.selSystemAllegiance);
        selSystemGov = (Spinner) v.findViewById(R.id.selSystemGov);
        selSystemEconomy = (Spinner) v.findViewById(R.id.selSystemEconomy);
        selSystemState = (Spinner) v.findViewById(R.id.selSystemState);
        selMaxDistance = (Spinner) v.findViewById(R.id.selMaxDistance);
        btnSystem = (Button) v.findViewById(R.id.btnSystem);
        btnFilterSystems = (Button) v.findViewById(R.id.btnFilterSystems);
        lvFilteredSystems = (ListView) v.findViewById(R.id.lvFilteredSystems);

        selSystemAllegiance.setAdapter(ArrayAdapter.createFromResource(
                getContext(), R.array.allegiance_options, android.R.layout.simple_list_item_1));
        selSystemGov.setAdapter(ArrayAdapter.createFromResource(
                getContext(), R.array.government_options, android.R.layout.simple_list_item_1));
        selSystemEconomy.setAdapter(ArrayAdapter.createFromResource(
                getContext(), R.array.economy_options, android.R.layout.simple_list_item_1));
        selSystemState.setAdapter(ArrayAdapter.createFromResource(
                getContext(), R.array.state_options, android.R.layout.simple_list_item_1));
        selMaxDistance.setAdapter(ArrayAdapter.createFromResource(
                getContext(), R.array.distance_options, android.R.layout.simple_list_item_1));

        btnSystem.setOnClickListener(this);
        btnFilterSystems.setOnClickListener(this);
    }

    @Override
    public void onAsyncTaskComplete(Task task, Object obj) {
        switch (task) {
            case SYSTEMS_LITE:
                if (obj != null && obj instanceof ArrayList<?>) {
                etSystemToFind.setAdapter(new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1,
                        android.R.id.text1, (ArrayList<?>)obj));
            }
                break;
            case SYSTEMS_FULL:
                if (obj != null && obj instanceof ArrayList<?>) {
                    this.sysResults = (ArrayList<System>) obj;
                    // Todo: check this works, then make custom row layout
                    this.lvFilteredSystems.setAdapter(
                            new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                                    android.R.id.text1, sysResults)
                    );
                }
                break;
            case SYSTEM_INFO:
                if (obj != null && obj instanceof System) {
                    System sys = (System) obj;

                } else {
                    Toast.makeText(getContext(), "System entered does not exist", Toast.LENGTH_SHORT).show();
                }
                break;
            case SYSTEM_COORDS:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSystem:
                new GetSystemInfoTask(this).execute(etSystemToFind.getText().toString());
                break;

            case R.id.btnFilterSystems:
                SharedPreferences settings = getActivity().getSharedPreferences(getActivity()
                        .getString(R.string.curr_system_setting), 0);


                //new GetSystemsExtendedTask(this,)
                break;
        }
    }
}
