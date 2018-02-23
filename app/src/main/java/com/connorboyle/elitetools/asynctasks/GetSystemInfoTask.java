package com.connorboyle.elitetools.asynctasks;

import android.util.Base64;
import android.util.Log;

import com.connorboyle.elitetools.models.System;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Connor Boyle on 22-Oct-17.
 */

public class GetSystemInfoTask extends ParseJsonTask<String, Void, System> {
    private static final String URL_BASE = "http://138.197.151.119/api/v1/systems?name=";

    public GetSystemInfoTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.SYSTEM_INFO);
    }

    @Override
    protected System doInBackground(String... params) {
        String sUrl = URL_BASE + params[0];
        System system = null;

        HttpURLConnection conn = null;
        BufferedReader br = null;
        Gson gson = new Gson();

        Log.d("URL string created: ", sUrl);

        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            InputStream is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            JsonReader jr = new JsonReader(br);

            jr.beginArray();
            system = gson.fromJson(jr, System.class);
            jr.endArray();

        } catch (MalformedURLException m) {
            m.printStackTrace();
            Log.d("URL string created: ", sUrl);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return system;
    }
}
