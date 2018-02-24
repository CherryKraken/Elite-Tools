package com.connorboyle.elitetools.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.adapters.MaterialBodiesAdapter;
import com.connorboyle.elitetools.adapters.MaterialSystemsAdapter;
import com.connorboyle.elitetools.asynctasks.GetIngredientsTask;
import com.connorboyle.elitetools.asynctasks.GetMaterialSystemsTask;
import com.connorboyle.elitetools.asynctasks.GetRawMaterialBodiesTask;
import com.connorboyle.elitetools.asynctasks.GetSystemsExtendedTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;
import com.connorboyle.elitetools.models.Material;
import com.connorboyle.elitetools.models.MaterialBodyVM;
import com.connorboyle.elitetools.models.MaterialSystemVM;
import com.connorboyle.elitetools.models.System;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 20-Sep-17.
 */

public class MaterialsFinderActivity extends Fragment implements OnTaskCompleteHelper, View.OnClickListener {
    private View v;

    private TextView tvRarity;
    private TextView tvHowToFind;
    private TextView tvWhereToFind;
    private Button btnFindMat;
    private AutoCompleteTextView etMatToFind;

    private ArrayList<Material> materials;
    private ArrayList<String> materialNames;

    private ListView lvResults;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mat_finder_layout, container, false);
        setupControls();
        new GetIngredientsTask(this).execute();
        return v;
    }

    private void setupControls() {
        tvRarity = (TextView) v.findViewById(R.id.tvRarity);
        tvHowToFind = (TextView) v.findViewById(R.id.tvHowToFind);
        tvWhereToFind = (TextView) v.findViewById(R.id.tvWhereToFind);
        etMatToFind = (AutoCompleteTextView) v.findViewById(R.id.etMatToFind);
        lvResults = (ListView) v.findViewById(R.id.lvResults);

        etMatToFind.setThreshold(1);

        btnFindMat = (Button) v.findViewById(R.id.btnFindMat);
        btnFindMat.setOnClickListener(this);
    }

    @Override
    public void onAsyncTaskComplete(Task task, Object obj) {
        switch (task) {
            case INGREDIENTS:
                materials = (ArrayList<Material>) obj;
                materialNames = new ArrayList<>();

                for (Material m : materials) {
                    materialNames.add(m.name);
                }
                etMatToFind.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, materialNames));
                break;
            case MATERIAL_BODIES:
                ArrayList<MaterialBodyVM> list = (ArrayList<MaterialBodyVM>) obj;
                lvResults.setAdapter(new MaterialBodiesAdapter(getContext(), list));
                break;
            case MATERIAL_SYSTEMS:
                ArrayList<MaterialSystemVM> list2 = (ArrayList<MaterialSystemVM>) obj;
                lvResults.setAdapter(new MaterialSystemsAdapter(getContext(), list2));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (materialNames.contains(etMatToFind.getText().toString())) {
            Material selection = materials.get(materialNames.indexOf(etMatToFind.getText().toString()));
            SharedPreferences settings = getActivity().getSharedPreferences(getActivity()
                    .getString(R.string.curr_system_setting), 0);

            tvRarity.setText("Grade: " + selection.getGrade());
            tvHowToFind.setText("How to obtain: " + selection.methodDesc);

            if (selection.type.equals("Raw")) {
                tvWhereToFind.setText("May be found on the following planets:");
                new GetRawMaterialBodiesTask(this, selection.name, settings.getString("CurSystem", "sol")).execute();
            } else if (selection.method.length() > 0) {
                tvWhereToFind.setText("May be found in the following systems:");
                new GetMaterialSystemsTask(this, selection.name, settings.getString("CurSystem", "sol")).execute();
            } else {
                tvWhereToFind.setText("May be found in most systems.");
            }
        }
    }
}
