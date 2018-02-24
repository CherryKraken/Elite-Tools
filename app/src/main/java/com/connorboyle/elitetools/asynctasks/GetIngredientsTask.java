package com.connorboyle.elitetools.asynctasks;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.connorboyle.elitetools.models.Material;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 30-Sep-17.
 */

public class GetIngredientsTask extends ParseJsonTask<Void, Void, ArrayList<Material>> {

    public GetIngredientsTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.INGREDIENTS);
    }

    @Override
    protected ArrayList<Material> doInBackground(Void... params) {
        return getIngredients();
    }

    private ArrayList<Material> getIngredients() {
        ArrayList<Material> ingredients = new ArrayList<>();

        try {
            InputStream is = ((Fragment) caller).getContext().getAssets().open("ingredients.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                Material m = new Material(jr.nextName(), "0");
                jr.beginObject();
                jr.nextName();
                m.grade = jr.nextString();
                jr.nextName();
                m.type = jr.nextString();
                jr.nextName();
                if (jr.peek() != JsonToken.NULL) {
                    m.method = jr.nextString();
                } else {
                    m.method = "";
                    jr.nextNull();
                }
                jr.nextName();
                if (jr.peek() != JsonToken.NULL) {
                    m.methodDesc = jr.nextString();
                } else {
                    m.methodDesc = "";
                    jr.nextNull();
                }
                Log.d("material parsed", m.name + " : " + m.type + " : " + m.grade);
                ingredients.add(m);
                jr.endObject();
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
}
