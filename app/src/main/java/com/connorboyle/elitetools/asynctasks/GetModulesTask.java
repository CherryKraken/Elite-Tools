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
 * Created by Connor Boyle on 13-Sep-17.
 *
 * AsyncTask to get a list of all ship modules that can be modified by engineers
 */

public class GetModulesTask extends ParseJsonTask<Void, Void, ArrayList<String>> {

    public GetModulesTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.MODULES);
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> moduleNames = new ArrayList<>();
        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("modules_index.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();
            while (jr.hasNext()) {
                moduleNames.add(jr.nextName());
                jr.beginObject();
                while (jr.hasNext()) {
                    if (jr.nextName().equals("Type")) {
                        moduleNames.add(jr.nextString());
                    } else {
                        jr.skipValue();
                    }
                }
                jr.endObject();
            }
            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moduleNames;
    }
}
