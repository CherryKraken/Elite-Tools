package com.connorboyle.elitetools.models;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 01-Oct-17.
 */

public class Material {
    private static final String[] Grades = {
            "Very Common",
            "Common",
            "Standard",
            "Rare",
            "Very Rare"
    };

    public String name;
    private String grade;
    private ArrayList<String> methods;

    public Material(String name, String grade) {
        this.name = name;
        this.grade = grade;
        methods = new ArrayList<>();
    }

    public void addMethod(String s) {
        methods.add(s);
    }

    public ArrayList<String> getMethods() {
        return methods;
    }

    public String getGrade() {
        return String.format("%s (%s)", grade, Grades[Integer.valueOf(grade) - 1]);
    }
}
