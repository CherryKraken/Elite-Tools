package com.connorboyle.elitetools;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Connor Boyle on 13-Sep-17.
 */

public class GetModulesTask extends AsyncTask<String, Void, ArrayList<String>> {
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        return getModuleList(params[0]);
    }

    private ArrayList<String> getModuleList(String url) {
        ArrayList<String> moduleNames = new ArrayList<>();
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            JsonParser jp = new JsonParser();
            jr.beginObject();
            while (jr.hasNext()) {
                moduleNames.add(jr.nextName());
                //moduleNames.add(jr.nextString());
                jr.skipValue();
            }
            jr.endObject();
            jr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moduleNames;
    }
}