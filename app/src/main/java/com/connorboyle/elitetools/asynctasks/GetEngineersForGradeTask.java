package com.connorboyle.elitetools.asynctasks;

import android.support.v4.app.Fragment;

import com.connorboyle.elitetools.classes.Recipe;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Connor Boyle on 28-Sep-17.
 */

public class GetEngineersForGradeTask extends ParseJsonTask<Recipe, Void, Recipe> {

    public GetEngineersForGradeTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.ENGINEERS_FOR_GRADE);
    }

    @Override
    protected Recipe doInBackground(Recipe... params) {
        Recipe recipe = params[0];

        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("modules_index.json");
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
