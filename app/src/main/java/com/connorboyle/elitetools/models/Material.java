package com.connorboyle.elitetools.models;

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
    public String grade;
    public String type;
    public String method;
    public String methodDesc;

    public Material(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getGrade() {
        return String.format("%s (%s)", grade, Grades[Integer.valueOf(grade) - 1]);
    }
}
