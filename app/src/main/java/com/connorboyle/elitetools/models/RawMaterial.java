package com.connorboyle.elitetools.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Connor Boyle on 01-Oct-17.
 */

public class RawMaterial extends Material {
    private HashMap<String, HashMap<String, Float>> bodies; // <system <body, ratio>>

    public RawMaterial(String name, String grade) {
        super(name, grade);
        bodies = new HashMap<>();
    }

    public void addBody(String system, String body, float ratio) {
        if (!bodies.containsKey(system)) {
            bodies.put(system, new HashMap<String, Float>());
        }
        bodies.get(system).put(body, ratio);
    }

    public ArrayList<String> getBodiesAsStrings() {
        ArrayList<String> list = new ArrayList<>();
        for (String system : bodies.keySet()) {
            for (String body : bodies.get(system).keySet()) {
                list.add(system + "," + body + "," + bodies.get(system).get(body));
            }
        }
        return list;
    }
}
