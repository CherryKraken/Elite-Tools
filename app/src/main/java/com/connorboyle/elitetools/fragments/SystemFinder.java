package com.connorboyle.elitetools.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.SystemInfoActivity;
import com.connorboyle.elitetools.adapters.HashMapAdapter;
import com.connorboyle.elitetools.adapters.SystemsArrayAdapter;
import com.connorboyle.elitetools.asynctasks.GetSystemCoordsTask;
import com.connorboyle.elitetools.asynctasks.GetSystemInfoTask;
import com.connorboyle.elitetools.asynctasks.GetSystemsExtendedTask;
import com.connorboyle.elitetools.asynctasks.GetSystemsLiteTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;
import com.connorboyle.elitetools.models.System;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 22-Oct-17.
 */

public class SystemFinder extends Fragment implements OnTaskCompleteHelper, View.OnClickListener, ListView.OnItemClickListener {

    private View v;
    private AutoCompleteTextView etSystemToFind;
    private Spinner selSystemAllegiance, selSystemGov, selSystemEconomy, selSystemState, selMaxDistance;
    private Button btnSystem, btnFilterSystems;

    ArrayList<System> sysResults;
    private ListView lvFilteredSystems;

    private System curSystem;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.system_finder_layout, container, false);
        setupControls();

        SharedPreferences settings = getActivity().getSharedPreferences(getActivity()
                .getString(R.string.curr_system_setting), 0);

        new GetSystemCoordsTask(this).execute(settings.getString("CurSystem", "sol"));
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
        lvFilteredSystems.setOnItemClickListener(this);
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
                            new SystemsArrayAdapter(getContext(), sysResults, this.curSystem));
                }
                break;
            case SYSTEM_INFO:
                if (obj != null && obj instanceof System) {
                    try {
                        System sys = (System) obj;
                        Intent intent = new Intent(getContext(), SystemInfoActivity.class)
                                .putExtra("systemInfo", sys);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "System entered does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "System entered does not exist or has not been added to the database", Toast.LENGTH_SHORT).show();
                }
                break;
            case SYSTEM_COORDS:
                if (obj != null && obj instanceof System) {
                    this.curSystem = (System) obj;
                }
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
                ArrayList<String> params = new ArrayList<>();

                if (selSystemAllegiance.getSelectedItemPosition() != 0)
                    params.add("allegiance=" + selSystemAllegiance.getSelectedItem());

                if (selSystemEconomy.getSelectedItemPosition() != 0)
                    params.add("economy=" + selSystemEconomy.getSelectedItem());

                if (selSystemGov.getSelectedItemPosition() != 0)
                    params.add("government=" + selSystemGov.getSelectedItem());

                if (selSystemState.getSelectedItemPosition() != 0)
                    params.add("state=" + selSystemState.getSelectedItem());

                params.add("maxdistance=" + selMaxDistance.getSelectedItem());

                new GetSystemsExtendedTask(this, settings.getString("CurSystem", "sol")).execute(params.toArray(new String[params.size()]));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), SystemInfoActivity.class)
                .putExtra("systemInfo", sysResults.get(position));
        startActivity(intent);
    }
}
