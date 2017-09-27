package com.connorboyle.elitetools;

import android.os.AsyncTask;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 23-Sep-17.
 *
 * AsyncTask to get a list of grades available to apply a given modification
 * A few modifications have only a grade 3 option, so returning a count will give unwanted results
 */

class GetGradesTask extends AsyncTask<String, Void, ArrayList<String>> {
    private BlueprintsActivity caller;

    GetGradesTask(BlueprintsActivity caller) {
        this.caller = caller;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        return getGradesList(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        caller.onBackgroundTaskCompleted(BlueprintsActivity.JSONTask.GRADES, strings);
    }

    private ArrayList<String> getGradesList(String modID) {
        ArrayList<String> gradesList = new ArrayList<>();
        try {
            InputStream is = caller.getContext().getAssets().open("blueprints_detail.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                if (jr.nextName().equals(modID)) {
                    jr.beginObject();
                    while (jr.hasNext()) {
                        gradesList.add(jr.nextName());
                        jr.skipValue();
                    }
                    jr.endObject();
                } else {
                    jr.skipValue();
                }
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
