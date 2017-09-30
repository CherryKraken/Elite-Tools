package com.connorboyle.elitetools.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.connorboyle.elitetools.R;

/**
 * Created by Connor Boyle on 09-Sep-17.
 */

public class HabZoneCalculator extends Fragment implements TextWatcher {

    View v;
    EditText numRads, numKelvin;
    TextView txtMinInner, txtInner, txtCenter, txtOuter, txtMaxOuter;
    private static final String LS = "Ls";

    // Solar measurements
    private static final double SOLAR_LUM = 2.828e26; // Watts
    private static final double SOLAR_TEMP = 5778.0; // Kelvin
    // Astronomical Units to Light-Seconds conversion factor
    private static final double AU_TO_LS = 499.05; // Light-seconds
    // Factors for best orbital distances around star
    private static final double MIN_DIST = 0.75;
    private static final double INNER_DIST = 0.95;
    private static final double CENTER_DIST = 1;
    private static final double OUTER_DIST = 1.37;
    private static final double MAX_DIST = 1.77;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.hz_calc_layout, container, false);
        setupControls();
        return v;
    }

    private void setupControls() {
        numRads = (EditText)v.findViewById(R.id.numRads);
        numRads.addTextChangedListener(this);
        numKelvin = (EditText)v.findViewById(R.id.numKelvin);
        numKelvin.addTextChangedListener(this);
        txtMinInner = (TextView)v.findViewById(R.id.txtMinInner);
        txtInner = (TextView)v.findViewById(R.id.txtInner);
        txtCenter = (TextView)v.findViewById(R.id.txtOptimal);
        txtOuter = (TextView)v.findViewById(R.id.txtOuter);
        txtMaxOuter = (TextView)v.findViewById(R.id.txtMaxOuter);
    }

    private double luminosityOf(double radius, double temp) {
        return Math.pow(radius, 2) * Math.pow(temp/SOLAR_TEMP, 4);
    }

    private double orbitalDistance(double bound, double radius, double temp) {
        return AU_TO_LS * bound * Math.sqrt(luminosityOf(radius, temp));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // unused
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        double r, t;
        try {
            if (isTextIn(numRads) && isTextIn(numKelvin)) {
                r = Double.parseDouble(numRads.getText().toString());
                t = Double.parseDouble(numKelvin.getText().toString());

                txtMinInner.setText((float)orbitalDistance(MIN_DIST, r, t) + " " + LS);
                txtInner.setText((float)orbitalDistance(INNER_DIST, r, t) + " " + LS);
                txtCenter.setText((float)orbitalDistance(CENTER_DIST, r, t) + " " + LS);
                txtOuter.setText((float)orbitalDistance(OUTER_DIST, r, t) + " " + LS);
                txtMaxOuter.setText((float)orbitalDistance(MAX_DIST, r, t) + " " + LS);
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // unused
    }

    private boolean isTextIn(EditText et) {
        return et.getText().toString().trim().length() > 0;
    }
}
