package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Connor Boyle on 13-Sep-17.
 */

public class BlueprintsActivity extends Fragment {
    private static final String SYSTEMS_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwZmNLWU52WUF3VXM";
    private static final String MODULES_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwdFFyNnQzRm9lMm8";
    private static final String ENGINEERS_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwR3ZrZFpLd2VGTzQ";
    private static final String INGREDIENTS_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwNWhuUzlNaGNPUk0";

    ArrayList<String> moduleList;
    GetModulesTask task;

    View v;

    Spinner spinnerParts;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.blueprint_layout, container, false);
        task = new GetModulesTask();
        startModulesTask();
        setupControls();
        return v;
    }

    private void startModulesTask() {
        task.execute(MODULES_JSON_URL);
        try {
            moduleList = task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setupControls() {
        spinnerParts = (Spinner) v.findViewById(R.id.spinnerParts);
        spinnerParts.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, moduleList));
    }
}
