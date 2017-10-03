package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;

import com.connorboyle.elitetools.fragments.EngineersActivity;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 02-Oct-17.
 */

public class GetEngineersTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private final EngineersActivity caller;

    public GetEngineersTask(EngineersActivity caller) {
        this.caller = caller;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> engrList = new ArrayList<>();

        try {
            InputStream is = caller.getContext().getAssets().open("engineers_detail.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                engrList.add(jr.nextName());
                jr.beginObject();
                while (jr.hasNext()) {
                    if (jr.nextName().equals("name")) {
                        engrList.add(jr.nextString());
                    } else {
                        jr.skipValue();
                    }
                }
                jr.endObject();
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch(IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        return engrList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        caller.onEngineersTaskCompleted(strings);
    }
}
