package com.connorboyle.elitetools;

import android.os.AsyncTask;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 23-Sep-17.
 */

public class GetGradesTask extends AsyncTask<String, Void, ArrayList<String>> {
    BlueprintsActivity caller;

    GetGradesTask(BlueprintsActivity caller) {
        this.caller = caller;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        return getGradesList(params[0], params[1], params[2]);
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        caller.onBackgroundTaskCompleted(BlueprintsActivity.JSONTask.GRADES, strings);
    }

    private ArrayList<String> getGradesList(String module, String modification, String url) {
        ArrayList<String> gradesList = new ArrayList<>();
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
            if (jr.nextName().equals("Engineers"))
                jr.skipValue();
            if (jr.nextName().equals("Experimental"))
                jr.skipValue();

            if (jr.nextName().equals("Blueprints")) {
                jr.beginArray();
                while (jr.hasNext()) {
                    jr.beginObject();
                    if (jr.nextName().equals("name") && jr.nextString().equals(modification)) {
                        while (jr.hasNext()) {
                            gradesList.add(jr.nextName());
                            jr.skipValue();
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
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        return gradesList;
    }
}
