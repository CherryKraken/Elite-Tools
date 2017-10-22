package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.connorboyle.elitetools.classes.Engineer;
import com.connorboyle.elitetools.fragments.EngineersActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Connor Boyle on 03-Oct-17.
 */

public class GetEngineerDetailTask extends ParseJsonTask <String, Void, Engineer> {

    public GetEngineerDetailTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.ENGINEER_INFO);
    }

    @Override
    protected Engineer doInBackground(String... params) {
        String engrID = params[0];
        Engineer engineer = null;

        try {
            InputStreamReader isr = new InputStreamReader(
                    ((Fragment)caller).getContext().getAssets().open("engineers_detail.json"));
            JsonReader jr = new JsonReader(isr);
            jr.beginObject();

            while (jr.hasNext() && !jr.nextName().equals(engrID)) {
                jr.skipValue();
            }

            JsonElement je = new JsonParser().parse(jr);
            engineer = new Gson().fromJson(je, Engineer.class);

            jr.close();
            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return engineer;
    }
}
