package com.connorboyle.elitetools.asynctasks;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.connorboyle.elitetools.models.System;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * AsyncTask to get a list of systems using the EliteBGS REST API implementation
 * <a>https://github.com/SayakMukhopadhyay/elitebgs</a>
 * <p>
 * Created by Connor Boyle on 21-Oct-17.
 */

public class GetSystemsExtendedTask extends ParseJsonTask<String, Void, ArrayList<System>> {
    private static final String URL_BASE = "http://138.197.151.119/api/v1/systems?";
    private static final String AND = "&";

    private final String curSystemName;

    public GetSystemsExtendedTask(OnTaskCompleteHelper caller, String curSystemName) {
        super(caller, OnTaskCompleteHelper.Task.SYSTEMS_FULL);
        this.curSystemName = curSystemName;
    }

    @Override
    protected ArrayList<System> doInBackground(String... params) {
        ArrayList<System> systems = new ArrayList<>();
        HttpURLConnection conn = null;
        BufferedReader br = null;
        Gson gson = new Gson();

        String sUrl;
        if (params.length > 0) {
            sUrl = URL_BASE + TextUtils.join(AND, params) + AND + "refsystem=" + curSystemName;
        }
        else {
            sUrl = URL_BASE + "refsystem=" + curSystemName;
        }


        Log.d("URL string created: ", sUrl);

        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            InputStream is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            systems = gson.fromJson(br, new TypeToken<ArrayList<System>>(){}.getType());
        } catch (MalformedURLException m) {
            m.printStackTrace();
            Log.d("URL string created: ", sUrl);
        } catch (IOException e) {
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

        return systems;
    }
}
