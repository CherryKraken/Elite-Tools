package com.connorboyle.elitetools;

import android.os.AsyncTask;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Connor Boyle on 23-Sep-17.
 */

public class GetRecipeTask extends AsyncTask<String, Void, Recipe> {
    private BlueprintsActivity caller;

    GetRecipeTask(BlueprintsActivity caller) {
        this.caller = caller;
    }

    @Override
    protected Recipe doInBackground(String... params) {
        return getRecipe(params[0], params[1], params[2], params[3]);
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        caller.onRecipeTaskCompleted(recipe);
    }

    private Recipe getRecipe(String module, String modification, String grade, String url) {
        Recipe recipe = new Recipe(module, modification, grade);
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();
            // loop until reader arrives at given module name
            while (jr.hasNext() && !module.equals(jr.nextName())) {
                jr.skipValue();
            }

            jr.beginObject();
            if (jr.nextName().equals("Module"))
                jr.skipValue(); // "Module" key will be removed in future

            if (jr.nextName().equals("Engineers")) {
                jr.beginArray();
                while (jr.hasNext()) {
                    jr.beginObject();
                    jr.nextName(); // == "id"
                    jr.skipValue(); // skip "id"
                    jr.nextName(); // == "name"
                    String e = jr.nextString();
                    jr.nextName(); // == "grade"
                    String g = jr.nextString();
                    recipe.addEngineer(e, g);
                    jr.endObject();
                }
                jr.endArray();
            }

            if (jr.nextName().equals("Experimental")) {
                jr.beginArray();
                while (jr.hasNext())
                    recipe.addExperimental(jr.nextString());
                jr.endArray();
            }

            if (jr.nextName().equals("Blueprints")) {
                jr.beginArray();
                while (jr.hasNext()) {
                    jr.beginObject();
                    if (jr.nextName().equals("name") && jr.nextString().equals(modification)) {
                        while (jr.hasNext()) {
                            if (jr.nextName().equals(grade)) {
                                jr.beginObject();

                                jr.nextName(); // == "ingredients"
                                jr.beginArray();
                                while (jr.hasNext()) {
                                    recipe.addIngredient(jr.nextString());
                                }
                                jr.endArray();

                                jr.nextName(); // == "effects"
                                jr.beginArray();
                                while (jr.hasNext()) {
                                    String s;
                                    double min, max;
                                    jr.beginObject();
                                    s = jr.nextName();
                                    jr.beginArray();
                                    min = jr.nextDouble();
                                    max = jr.nextDouble();
                                    jr.endArray();
                                    jr.endObject();
                                    recipe.addEffect(s, min, max);
                                }
                                jr.endArray();

                                jr.endObject();
                            } else {
                                jr.skipValue();
                            }
                        }
                    } else {
                        while (jr.hasNext()) {
                            jr.skipValue();
                        }
                    }
                    jr.endObject();
                }
                jr.endArray();
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
