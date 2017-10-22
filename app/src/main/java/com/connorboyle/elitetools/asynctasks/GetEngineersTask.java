package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

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

public class GetEngineersTask extends ParseJsonTask<Void, Void, ArrayList<String>> {

    public GetEngineersTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.ENGINEERS);
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> engrList = new ArrayList<>();

        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("engineers_detail.json");
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
}
