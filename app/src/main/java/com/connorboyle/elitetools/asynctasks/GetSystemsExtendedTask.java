package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.connorboyle.elitetools.classes.System;
import com.connorboyle.elitetools.fragments.MaterialsFinderActivity;
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

/**
 * AsyncTask to get a list of systems using the EliteBGS REST API implementation
 * <a>https://github.com/SayakMukhopadhyay/elitebgs</a>
 * <p>
 * Created by Connor Boyle on 21-Oct-17.
 */

public class GetSystemsExtendedTask extends ParseJsonTask<String, Void, ArrayList<System>> {
    private static final String URL_BASE = "http://elitebgs.kodeblox.com/api/eddb/v1/systems?";
    private static final String AND = "&";
    private static final String NEXTID = "idnext=";

    private static final String BASIC_AUTH =
            "Basic " + Base64.encodeToString("guest:secret".getBytes(), Base64.NO_WRAP);

    private final int mMaxDistance;
    private final System curSystem;
    @Nullable
    private System lastSystemListed;

    public GetSystemsExtendedTask(OnTaskCompleteHelper caller, System curSystem, @Nullable System lastListed, int maxDist) {
        super(caller, OnTaskCompleteHelper.Task.SYSTEMS_FULL);
        this.mMaxDistance = maxDist;
        this.curSystem = curSystem;
        this.lastSystemListed = lastListed;
    }

    @Override
    protected ArrayList<System> doInBackground(String... params) {
        ArrayList<System> systems = new ArrayList<>();
        HttpURLConnection conn = null;
        BufferedReader br = null;
        Gson gson = new Gson();

        String sUrl = URL_BASE + TextUtils.join(AND, params);

        if (lastSystemListed != null) {
            sUrl += AND + NEXTID + lastSystemListed._id;
        }

        Log.d("URL string created: ", sUrl);

        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", BASIC_AUTH);
            conn.connect();

            InputStream is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            systems = gson.fromJson(br, new TypeToken<ArrayList<System>>(){}.getType());

            // Not sure how well this will work with more than two pages
            if (this.lastSystemListed != null && systems.contains(this.lastSystemListed)) {
                return null;
            }

            for (System sys : systems) {
                if (Math.abs(curSystem.distanceTo(sys)) > mMaxDistance) {
                    systems.remove(sys);
                }
            }
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
