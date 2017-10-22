package com.connorboyle.elitetools.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetIngredientsTask;
import com.connorboyle.elitetools.asynctasks.GetSystemsExtendedTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;
import com.connorboyle.elitetools.classes.Material;
import com.connorboyle.elitetools.classes.System;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 20-Sep-17.
 */

public class MaterialsFinderActivity extends Fragment implements OnTaskCompleteHelper {
    private View v;

    private TextView tvRarity;
    private AutoCompleteTextView etMatToFind, etCurSystem;

    private ArrayList<String> materials;
    private ArrayList<System> systems;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mat_finder_layout, container, false);
        systems = new ArrayList<>();
        setupControls();
        new GetIngredientsTask(this).execute();
        new GetSystemsExtendedTask(this, new System("Sol", 0,0,0), null, 500).execute(
                "statename=outbreak", "allegiancename=independent");
        return v;
    }

    private void setupControls() {
        tvRarity = (TextView) v.findViewById(R.id.tvRarity);
        etMatToFind = (AutoCompleteTextView) v.findViewById(R.id.etMatToFind);
        etCurSystem = (AutoCompleteTextView) v.findViewById(R.id.etCurSystem);

        tvRarity.setVisibility(View.INVISIBLE);
        etCurSystem.setVisibility(View.GONE);

        etMatToFind.setThreshold(2);
        etCurSystem.setThreshold(2);

        etMatToFind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onMaterialSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (materials.contains(etMatToFind.getText().toString())) {
                    onMaterialSelected(materials.indexOf(etMatToFind.getText().toString()));
                }
            }
        });
    }

    private void onMaterialSelected(int position) {
        //new GetIngredientInfoTask(this).execute(materials.get(position));
    }

    //    public void onSystemListTaskCompleted(ArrayList<System> list) {
//        if (list != null && list.size() > 1) {
//            this.systems.addAll(list);
//            new GetSystemsExtendedTask(this, new System("Sol", 0,0,0), systems.get(systems.size()-1), 500).execute(
//                    "statename=outbreak", "allegiancename=independent");
//        } else {
//            ArrayList<String> strings = new ArrayList<>();
//            for (System s : systems) {
//                strings.add(String.format("%s (%f)", s.name, s.distanceTo(new System("Sol", 0,0,0))));
//            }
//            ((ListView) v.findViewById(R.id.lvRelSystems)).setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, strings));
//            Toast.makeText(getContext(), strings.size() + "", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onAsyncTaskComplete(Task task, Object obj) {
        switch (task) {
            case INGREDIENTS:
                materials = (ArrayList<String>) obj;
                etMatToFind.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, materials));
                break;
            case INGREDIENT_INFO:
                break;
        }
    }
}
