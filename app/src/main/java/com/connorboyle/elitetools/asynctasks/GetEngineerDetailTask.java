package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;

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

public class GetEngineerDetailTask extends AsyncTask <String, Void, Engineer> {
    private final EngineersActivity caller;

    public GetEngineerDetailTask(EngineersActivity caller) {
        this.caller = caller;
    }
    @Override
    protected Engineer doInBackground(String... params) {
        String engrID = params[0];
        Engineer engineer = null;

        try {
            InputStreamReader isr = new InputStreamReader(
                    caller.getContext().getAssets().open("engineers_detail.json"));
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

    @Override
    protected void onPostExecute(Engineer engineer) {
        caller.onEngineerObjectCreated(engineer);
    }
}
