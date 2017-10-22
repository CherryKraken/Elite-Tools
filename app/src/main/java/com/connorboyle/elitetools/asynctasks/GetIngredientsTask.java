package com.connorboyle.elitetools.asynctasks;

import android.support.v4.app.Fragment;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 30-Sep-17.
 */

public class GetIngredientsTask extends ParseJsonTask<Void, Void, ArrayList<String>> {

    public GetIngredientsTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.INGREDIENTS);
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        return getIngredients();
    }

    private ArrayList<String> getIngredients() {
        ArrayList<String> ingredients = new ArrayList<>();

        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("ingredients.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                ingredients.add(jr.nextName());
                jr.skipValue();
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch(IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
}
