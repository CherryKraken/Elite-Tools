package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.connorboyle.elitetools.fragments.BlueprintsActivity;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 22-Sep-17.
 *
 * AsyncTask to get a list of all engineer modifications for a given module
 */

public class GetModificationsTask extends ParseJsonTask<String, Void, ArrayList<String>> {
    public GetModificationsTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.MODIFICATIONS);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        final String module = params[0];

        ArrayList<String> modsList = new ArrayList<>();

        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("modules_index.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();
            // loop until reader arrives at given module name
            while (jr.hasNext() && !module.equals(jr.nextName())) {
                jr.skipValue();
            }

            jr.beginObject();
            while (jr.hasNext()) {
                if (jr.nextName().equals("Blueprints")) {
                    jr.beginArray();

                    while (jr.hasNext()) {
                        modsList.add(jr.nextString());
                    }

                    jr.endArray();
                    jr.close();
                    is.close();
                    return modsList;
                } else {
                    jr.skipValue();
                }
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch(IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        return modsList;
    }
}
