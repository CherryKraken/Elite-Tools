package com.connorboyle.elitetools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Connor Boyle on 23-Sep-17.
 */

class Recipe implements Serializable {
    String module, modification, grade, title;
    ArrayList<String> ingredients, experimentals;
    HashMap<String, String> engineers;
    HashMap<String, double[]> effects; // <"effect": [min, max]>

    Recipe(String module, String modification, String grade) {
        this.module = module;
        this.modification = modification;
        this.grade = grade;
        this.title = String.format("%s: Grade %s %s", module, grade, modification);

        ingredients = new ArrayList<>();
        experimentals = new ArrayList<>();
        engineers = new HashMap<>();
        effects = new HashMap<>();
    }

    void addIngredient(String s) { ingredients.add(s); }
    void addExperimental(String s) { experimentals.add(s); }
    void addEngineer(String e, String g) {
        if (Integer.valueOf(g) >= Integer.valueOf(this.grade)) { // Ignore engineers that can't perform modification
            engineers.put(e, g);
        }
    }
    void addEffect(String s, double min, double max) {
        effects.put(s, new double[] { min, max });
    }
}
