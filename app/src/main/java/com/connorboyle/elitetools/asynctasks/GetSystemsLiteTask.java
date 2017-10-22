package com.connorboyle.elitetools.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 21-Oct-17.
 */

public class GetSystemsLiteTask extends ParseJsonTask<Void, Void, ArrayList<String>> {
    public GetSystemsLiteTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.SYSTEMS_LITE);
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> systemNames = new ArrayList<>();
        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("system-coords-keyed.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                systemNames.add(jr.nextName());
                jr.skipValue();
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch(IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        return (systemNames.size() > 0) ? systemNames : null;
    }
}
