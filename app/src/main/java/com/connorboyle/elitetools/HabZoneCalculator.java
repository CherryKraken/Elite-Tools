package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Connor Boyle on 09-Sep-17.
 */

public class HabZoneCalculator extends Fragment {

    View v;

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
        EditText numRads = (EditText)v.findViewById(R.id.numRads);
        EditText numKelvin = (EditText)v.findViewById(R.id.numKelvin);
    }

    private double luminosityOf(double radius, double temp) {
        return Math.pow(radius, 2) * Math.pow(temp/SOLAR_TEMP, 4);
    }

    private double orbitalDistance(double bound, double radius, double temp) {
        return AU_TO_LS * bound * Math.sqrt(luminosityOf(radius, temp));
    }
}
