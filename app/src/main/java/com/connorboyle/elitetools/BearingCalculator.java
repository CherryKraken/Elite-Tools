package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 03-Jun-17.
 */

public class BearingCalculator extends Fragment implements TextWatcher, Spinner.OnItemSelectedListener {

    View v;
    EditText numMyLat, numMyLng, numDestLat, numDestLng;
    TextView tvIBearing, tvFBearing;
    Spinner spinner;

    ArrayList<CoordPreset> presets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presets = new ArrayList<>();
        presets.add(new CoordPreset("Custom", null));
        presets.add(new CoordPreset("Pleiades Sector AB-W B2-4 - 9 A\nAlien Crash Site 1\n", new LatLng(-26.37, 97.7)));
        presets.add(new CoordPreset("HIP 17862 - 6 C A\nAlien Crash Site 2\n", new LatLng(30.32, -98.58)));
        presets.add(new CoordPreset("HIP 17403 - A 4 A\nAlien Crash Site 3\n", new LatLng(-34.98, -141.41)));
        presets.add(new CoordPreset("Synuefe XR-H d11-102 - 1 B\nAncient Ruins 1\n", new LatLng(-31.806, -128.937)));

        v = inflater.inflate(R.layout.bearing_calc_layout, container, false);
        setupControls();
        return v;
    }

    private void setupControls() {
        numMyLat = (EditText) v.findViewById(R.id.numMyLat);
        numMyLng = (EditText) v.findViewById(R.id.numMyLng);
        numDestLat = (EditText) v.findViewById(R.id.numDestLat);
        numDestLng = (EditText) v.findViewById(R.id.numDestLng);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        tvIBearing = (TextView) v.findViewById(R.id.tvIBearing);
        tvFBearing = (TextView) v.findViewById(R.id.tvFBearing);

        numMyLat.addTextChangedListener(this);
        numMyLng.addTextChangedListener(this);
        numDestLat.addTextChangedListener(this);
        numDestLng.addTextChangedListener(this);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, presets);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private double bearingDegrees(LatLng a, LatLng b) {
        double phi1 = Math.toRadians(a.latitude);
        double phi2 = Math.toRadians(b.latitude);
        double lam1 = Math.toRadians(a.longitude);
        double lam2 = Math.toRadians(b.longitude);

        double y = Math.sin(lam2 - lam1) * Math.cos(phi2);
        double x = (Math.cos(phi1) * Math.sin(phi2)) - (Math.sin(phi1) * Math.cos(phi2)) * Math.cos(lam2 - lam1);

        return Math.toDegrees(Math.atan2(y, x));
    }

    private double initialBearing(LatLng a, LatLng b) {
        return (bearingDegrees(a, b) + 360) % 360;
    }

    private double finalBearing(LatLng a, LatLng b) {
        return (bearingDegrees(b, a) + 180) % 360;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isTextIn(numMyLat) && isTextIn(numMyLng)
                && isTextIn(numDestLat) && isTextIn(numDestLng)) {

            try {
                LatLng a = new LatLng(
                        Double.parseDouble(numMyLat.getText().toString()),
                        Double.parseDouble(numMyLng.getText().toString()));

                LatLng b = new LatLng(
                        Double.parseDouble(numDestLat.getText().toString()),
                        Double.parseDouble(numDestLng.getText().toString()));

                tvIBearing.setText((float) initialBearing(a, b) + " \u00b0");
                tvFBearing.setText((float) finalBearing(a, b) + " \u00b0");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isTextIn(EditText et) {
        return et.getText().toString().trim().length() > 0;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Unused
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Unused
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            numDestLat.setEnabled(true);
            numDestLng.setEnabled(true);
        } else {
            CoordPreset sel = presets.get(position);
            numDestLat.setText(sel.latLng.latitude + "");
            numDestLng.setText(sel.latLng.longitude + "");
            numDestLat.setEnabled(false);
            numDestLng.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
