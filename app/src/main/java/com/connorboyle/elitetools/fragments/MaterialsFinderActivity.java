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
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetIngredientsTask;
import com.connorboyle.elitetools.classes.Material;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 20-Sep-17.
 */

public class MaterialsFinderActivity extends Fragment {
    private View v;

    private TextView tvRarity;
    private AutoCompleteTextView etMatToFind, etCurSystem;

    private ArrayList<String> materials;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mat_finder_layout, container, false);
        setupControls();
        new GetIngredientsTask(this).execute();
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

    public void onIngredientsTaskCompleted(ArrayList<String> strings) {
        materials = strings;
        etMatToFind.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, materials));
    }

    public void onMaterialInfoTaskCompleted(Material m) {

    }
}
