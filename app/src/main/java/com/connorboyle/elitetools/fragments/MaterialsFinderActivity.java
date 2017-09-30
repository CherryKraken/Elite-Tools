package com.connorboyle.elitetools.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.connorboyle.elitetools.R;

/**
 * Created by Connor Boyle on 20-Sep-17.
 */

public class MaterialsFinderActivity extends Fragment {
    private View v;

    private TextView tvRarity;
    private AutoCompleteTextView etMatToFind, etCurSystem;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.blueprint_layout, container, false);
        setupControls();
        return v;
    }

    private void setupControls() {
        tvRarity = (TextView) v.findViewById(R.id.tvRarity);
        etMatToFind = (AutoCompleteTextView) v.findViewById(R.id.etMatToFind);
        etCurSystem = (AutoCompleteTextView) v.findViewById(R.id.etCurSystem);
    }

    private double distBetween(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    }
}
