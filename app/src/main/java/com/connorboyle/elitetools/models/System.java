package com.connorboyle.elitetools.models;

import java.io.Serializable;

/**
 * Created by Connor Boyle on 30-Sep-17.
 */

public class System implements Serializable {
    public long id;
    public long eddbId;
    public long edsmId;
    public String name;
    public double x, y, z;
    public boolean needsPermit;
    public String updatedAt;
    public String simbadRef;
    public boolean isPopulated;
    public long population;
    public String allegiance;
    public String security;
    public String reserves;
    public String primaryEconomy;
    public String government;
    public String state;
    public String powerPlayLeader;
    public String powerPlayState;
    public long controllingMinorFactionId;

    public System(String name, double x, double y, double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Calculate distance in lightyears to the given system
     * @param dest
     * @return
     */
    public double distanceTo(System dest) {
        return Math.sqrt(
                Math.pow(dest.x - this.x, 2)
                + Math.pow(dest.y - this.y, 2)
                + Math.pow(dest.z - this.z, 2)
        );
    }

    private double distanceToRefSystem;
    public double getDistanceToRefSystem() { return distanceToRefSystem; }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof System) {
            return this.id == ((System) obj).id;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
