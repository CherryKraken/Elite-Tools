package com.connorboyle.elitetools.classes;

import android.support.annotation.NonNull;

/**
 * Created by Connor Boyle on 30-Sep-17.
 */

public class System {
    public String name;
    private double x, y, z;

    public System(String name, double x, double y, double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceTo(System dest) {
        return Math.sqrt(Math.pow(dest.x - this.x, 2) + Math.pow(dest.y - this.y, 2) + Math.pow(dest.z - this.z, 2));
    }
}
