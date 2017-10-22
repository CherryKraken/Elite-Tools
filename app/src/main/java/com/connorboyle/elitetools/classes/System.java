package com.connorboyle.elitetools.classes;

/**
 * Created by Connor Boyle on 30-Sep-17.
 */

public class System {
    public String _id; // use this ID for EliteBGS requests
    public long id;
    public String name_lower;
    public long edsm_id;
    public String name;
    public double x, y, z;
    public long population;
    public boolean is_populated;
    public int government_id;
    public String government; // e.g. corporation
    public int allegiance_id;
    public String allegiance; // e.g. empire
    public int state_id;
    public String state; // e.g. outbreak
    public int security_id;
    public String security; // e.g. medium
    public int primary_economy_id;
    public String primary_economy; // e.g. extraction
    public String power; // e.g. li yong-rui
    public String power_state; // e.g. control
    public int power_state_id;
    public boolean needs_permit; // e.g. Sol, Alioth, Vega, etc.
    public String updated_at; // e.g. 2017-10-18T13:15:26.000Z
    public String simbad_ref;
    public int controlling_minor_faction_id;
    public String controlling_minor_faction; // e.g.
    public int reserve_type_id;
    public String reserve_type; // e.g. depleted

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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof System) {
            return this._id == ((System) obj)._id;
        }
        return false;
    }
}
