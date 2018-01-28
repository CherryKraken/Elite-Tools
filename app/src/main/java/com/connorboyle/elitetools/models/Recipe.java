package com.connorboyle.elitetools.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Connor Boyle on 23-Sep-17.
 */

public class Recipe implements Serializable {
    public String module, modification, grade, title;
    public ArrayList<String> ingredients, experimentals;
    public HashMap<String, String> engineers; // <id, name>
    public HashMap<String, double[]> effects; // <"effect", [min, max]>

    public Recipe(String module, String modification, String grade) {
        this.module = module;
        this.modification = modification;
        this.grade = grade;
        this.title = String.format("%s: Grade %s %s", module, grade, modification);

        ingredients = new ArrayList<>();
        experimentals = new ArrayList<>();
        engineers = new HashMap<>();
        effects = new HashMap<>();
    }

    public void addIngredient(String s) {
        ingredients.add(s);
    }
    public void addExperimental(String s) {
        experimentals.add(s);
    }
    public void addEngineer(String id, String name) {
        engineers.put(id, name);
    }
    public void addEffect(String effect, double min, double max) {
        if (module.contains("Laser") && effect.equals("Ammo Clip Size")) {
            return; // For Overcharged laser weapons
        }
        if (module.contains("Beam") && effect.equals("Rate of Fire")) {
            return; // This check may be redundant
        }
        effects.put(effect, new double[] { min, max });
    }
}
