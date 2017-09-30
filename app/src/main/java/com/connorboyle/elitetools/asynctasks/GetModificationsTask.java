package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;

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

public class GetModificationsTask extends AsyncTask<String, Void, ArrayList<String>> {
    private final BlueprintsActivity caller;

    public GetModificationsTask(BlueprintsActivity caller) {
        this.caller = caller;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        return getModsList(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        caller.onBackgroundTaskCompleted(BlueprintsActivity.JSONTask.MODIFICATIONS, strings);
    }

    private ArrayList<String> getModsList(String module) {
        ArrayList<String> modsList = new ArrayList<>();
        try {
            InputStream is = caller.getContext().getAssets().open("modules_index.json");
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
