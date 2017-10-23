package com.connorboyle.elitetools.fragments;

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

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetSystemsLiteTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 22-Oct-17.
 */

public class SystemFinder extends Fragment implements OnTaskCompleteHelper {

    private View v;
    AutoCompleteTextView etSystemToFind;
    Spinner selSystemAllegiance, selSystemGov, selSystemEconomy, selSystemState, selMaxDistance;
    Button btnSystem, btnFilterSystems;

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
                break;
            case SYSTEM_INFO:
                break;
        }
    }
}
