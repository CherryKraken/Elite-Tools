package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.connorboyle.elitetools.fragments.BlueprintsActivity;
import com.connorboyle.elitetools.classes.Recipe;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Connor Boyle on 23-Sep-17.
 *
 * AsyncTask to get the blueprint recipe for the given module, modification, and grade.
 * Recipe includes the ingredients, and the effects and their range of possible values.
 * Does not include secondary or experimental effects.
 */

public class GetRecipeTask extends ParseJsonTask<String, Void, Recipe> {

    public GetRecipeTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.RECIPE);
    }

    @Override
    protected Recipe doInBackground(String... params) {
        final String module = params[0];
        final String type = params[1];
        final String modification = params[2];
        final String grade = params[3];

        Recipe recipe = new Recipe(module, modification, grade);
        String modID = (modification +"-"+ type).toLowerCase().replace(' ', '-');

        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("blueprints_detail.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();
            // loop until reader arrives at given modification name
            while (jr.hasNext()) {
                if (jr.nextName().equals(modID)) {
                    jr.beginObject();
                    while (jr.hasNext()) {
                        if (jr.nextName().equals(grade)) {
                            jr.beginObject();

                            // Add ingredients to recipe
                            if (jr.nextName().equals("ingredients")) {
                                jr.beginArray();
                                while (jr.hasNext()) {
                                    recipe.addIngredient(jr.nextString());
                                }
                                jr.endArray();
                            }

                            // Add effects to recipe
                            if (jr.nextName().equals("effects")) {
                                jr.beginArray();
                                while (jr.hasNext()) {
                                    jr.beginObject();
                                    String name = jr.nextName();
                                    jr.beginArray();
                                    double min = jr.nextDouble();
                                    double max = jr.nextDouble();
                                    jr.endArray();
                                    jr.endObject();
                                    recipe.addEffect(name, min, max);
                                }
                                jr.endArray();
                            }
                            jr.endObject();
                        } else {
                            jr.skipValue();
                        }
                    }
                    jr.endObject();
                } else {
                    jr.skipValue();
                }
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }
}
