package com.connorboyle.elitetools;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 13-Sep-17.
 */

public class BlueprintsActivity extends Fragment {
    private static final String SYSTEMS_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwZmNLWU52WUF3VXM";
    private static final String MODULES_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwdFFyNnQzRm9lMm8";
    private static final String ENGINEERS_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwR3ZrZFpLd2VGTzQ";
    private static final String INGREDIENTS_JSON_URL = "https://drive.google.com/uc?export=view&id=0B3a2T7y-8CHwNWhuUzlNaGNPUk0";

    enum JSONTask { MODULES, MODIFICATIONS, GRADES }

    private ArrayList<String> moduleList, modsList;

    private View v;

    private Spinner selModules, selModifications;
    private RadioGroup rbGrades;
    private RadioButton[] rbArray;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.blueprint_layout, container, false);
        new GetModulesTask(this).execute(MODULES_JSON_URL);
        setupControls();
        return v;
    }

    void onBackgroundTaskCompleted(JSONTask taskCompleted, ArrayList<String> strings) {
        switch (taskCompleted) {
            case MODULES:
                moduleList = strings;
                moduleList.add(0,"Select a ship module");
                selModules.setAdapter(new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1, moduleList));
                break;
            case MODIFICATIONS:
                modsList = strings;
                modsList.add(0, "Select a modification");
                selModifications.setAdapter(new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1, modsList));
                break;
            case GRADES:
                for (int i = 0; i < strings.size(); i++) {
                    rbArray[i].setEnabled(true);
                }
                break;
        }
    }

    void onRecipeTaskCompleted(Recipe recipe) {
        Bundle b = new Bundle();
        b.putSerializable("recipe", recipe);
        DialogFragment dialog = new RecipeDialog();
        dialog.setArguments(b);
        dialog.show(getActivity().getFragmentManager(), "recipe");
    }

    private void setupControls() {
        selModules = (Spinner) v.findViewById(R.id.selModules);
        selModifications = (Spinner) v.findViewById(R.id.selModifications);
        rbGrades = (RadioGroup) v.findViewById(R.id.rbGrades);
        rbArray = new RadioButton[]{
                (RadioButton) v.findViewById(R.id.rb1),
                (RadioButton) v.findViewById(R.id.rb2),
                (RadioButton) v.findViewById(R.id.rb3),
                (RadioButton) v.findViewById(R.id.rb4),
                (RadioButton) v.findViewById(R.id.rb5),
        };
        disableRadioButtons();

        selModules.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String module = moduleList.get(position);
                    new GetModificationsTask(getThisClass()).execute(module, MODULES_JSON_URL);
                } else {
                    selModifications.setAdapter(null);
                    disableRadioButtons();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        selModifications.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String module = selModules.getSelectedItem().toString();
                    String modification = modsList.get(position);
                    new GetGradesTask(getThisClass()).execute(module, modification, MODULES_JSON_URL);
                } else {
                    disableRadioButtons();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        rbGrades.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                String grade = rb.getText().toString();
                String module = selModules.getSelectedItem().toString();
                String modification = selModifications.getSelectedItem().toString();
                new GetRecipeTask((getThisClass())).execute(module, modification, grade, MODULES_JSON_URL);
            }
        });
    }

    private void disableRadioButtons() {
        for (RadioButton rb : rbArray) {
            rb.setEnabled(false);
        }
    }

    protected BlueprintsActivity getThisClass() { return this; }
}
