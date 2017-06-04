package com.connorboyle.elitetools;

/**
 * Created by Connor Boyle on 03-Jun-17.
 */

class CoordPreset {

    String key;
    LatLng latLng;

    CoordPreset(String key, LatLng latLng) {
        this.key = key;
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return key;
    }
}
