package com.connorboyle.elitetools;

import android.os.AsyncTask;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Connor Boyle on 28-Sep-17.
 */

public class GetEngineersForGradeTask extends AsyncTask<Recipe, Void, Recipe> {
    BlueprintsActivity caller;

    public GetEngineersForGradeTask(BlueprintsActivity caller) {
        this.caller = caller;
    }

    @Override
    protected Recipe doInBackground(Recipe... params) {
        return getEngineers(params[0]);
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        caller.onRecipeTaskCompleted(recipe);
    }

    private Recipe getEngineers(Recipe recipe) {
        try {
            InputStream is = caller.getContext().getAssets().open("modules_index.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                if (jr.nextName().equals(recipe.module)) {
                    jr.beginObject();
                    while (jr.hasNext()) {
                        if (jr.nextName().equals("Engineers")) {
                            jr.beginArray();
                            while (jr.hasNext()) {
                                jr.beginObject();
                                String eng = "", id = "";

                                if (jr.nextName().equals("id")) {
                                    id = jr.nextString();
                                }
                                if (jr.nextName().equals("name")) {
                                    eng = jr.nextString();
                                }
                                if (jr.nextName().equals("grade")) {
                                    int g = Integer.valueOf(jr.nextString());
                                    if (g >= Integer.valueOf(recipe.grade)) {
                                        recipe.addEngineer(id, eng);
                                    }
                                }
                                jr.endObject();
                            }
                            jr.endArray();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }
}
