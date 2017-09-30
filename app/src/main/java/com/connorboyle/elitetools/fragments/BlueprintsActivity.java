package com.connorboyle.elitetools.fragments;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetEngineersForGradeTask;
import com.connorboyle.elitetools.asynctasks.GetGradesTask;
import com.connorboyle.elitetools.asynctasks.GetModificationsTask;
import com.connorboyle.elitetools.asynctasks.GetModulesTask;
import com.connorboyle.elitetools.asynctasks.GetRecipeTask;
import com.connorboyle.elitetools.classes.Recipe;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 13-Sep-17.
 */

public class BlueprintsActivity extends Fragment {

    public enum JSONTask { MODULES, MODIFICATIONS, GRADES, RECIPE }

    private ArrayList<String> moduleList, modsList, typeList;

    private View v;

    private Spinner selModules, selModifications;
    private RadioGroup rbGrades;
    private RadioButton[] rbArray;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.blueprint_layout, container, false);
        new GetModulesTask(this).execute();
        setupControls();
        return v;
    }

    public void onBackgroundTaskCompleted(JSONTask taskCompleted, ArrayList<String> strings) {
        switch (taskCompleted) {
            case MODULES:
                moduleList = new ArrayList<>();
                typeList = new ArrayList<>();
                int i = 0;
                try {
                    while (i < strings.size()) {
                        moduleList.add(strings.get(i++));
                        typeList.add(strings.get(i++));
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                moduleList.add(0,"Select a ship module...");
                selModules.setAdapter(new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1, moduleList));
                break;
            case MODIFICATIONS:
                modsList = strings;
                modsList.add(0, "Select a modification...");
                selModifications.setAdapter(new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1, modsList));
                break;
            case GRADES:
                disableRadioButtons();
                rbGrades.setEnabled(true);
                for (String s : strings) {
                    rbArray[Integer.valueOf(s) - 1].setEnabled(true);
                }
                break;
        }
    }

    public void onRecipeTaskCompleted(Recipe recipe) {
        if (recipe.engineers.isEmpty()) {
            new GetEngineersForGradeTask(this).execute(recipe);
        } else {
            Bundle b = new Bundle();
            b.putSerializable("recipe", recipe);
            DialogFragment dialog = new RecipeDialog();
            dialog.setArguments(b);
            dialog.show(getActivity().getFragmentManager(), "recipe");
        }
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
                    new GetModificationsTask(getThisClass()).execute(module);
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
                    String type = typeList.get(moduleList.indexOf(module) - 1); // remember the default selection
                    String modification = modsList.get(position);
                    String result = (modification +"-"+ type).toLowerCase().replace(' ', '-');
                    new GetGradesTask(getThisClass()).execute(result);
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
                if (group.isEnabled()) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    String grade = rb.getText().toString();
                    String module = selModules.getSelectedItem().toString();
                    String type = typeList.get(moduleList.indexOf(module) - 1);
                    String modification = selModifications.getSelectedItem().toString();
                    new GetRecipeTask((getThisClass())).execute(module, type, modification, grade);
                    ((RadioButton) v.findViewById(checkedId)).setChecked(false);
                }
            }
        });
    }

    private void disableRadioButtons() {
        for (RadioButton rb : rbArray) {
            rb.setSelected(false);
            rb.setChecked(false);
            rb.setEnabled(false);
        }
    }

    protected BlueprintsActivity getThisClass() { return this; }
}
