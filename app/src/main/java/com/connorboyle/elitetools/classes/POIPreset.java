package com.connorboyle.elitetools.classes;

import com.connorboyle.elitetools.classes.LatLng;

/**
 * Created by Connor Boyle on 03-Jun-17.
 */

public class POIPreset {

    private String key;
    public LatLng latLng;

    public POIPreset(String key, LatLng latLng) {
        this.key = key;
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return key;
    }
}
