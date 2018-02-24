package com.connorboyle.elitetools.asynctasks;

import android.text.TextUtils;
import android.util.Log;

import com.connorboyle.elitetools.models.MaterialBodyVM;
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

/**
 * Created by Connor Boyle on 23-Feb-18.
 */

public class GetRawMaterialBodiesTask extends ParseJsonTask<Void, Void, ArrayList<MaterialBodyVM>> {
    private static final String URL_BASE = "http://138.197.151.119/api/v1/materials/";
    private static final String AND = "&";

    private final String curSystemName;
    private final String matToFind;

    public GetRawMaterialBodiesTask(OnTaskCompleteHelper caller, String matToFind, String curSystemName) {
        super(caller, OnTaskCompleteHelper.Task.MATERIAL_BODIES);
        this.curSystemName = curSystemName;
        this.matToFind = matToFind;
    }

    @Override
    protected ArrayList<MaterialBodyVM> doInBackground(Void... voids) {
        ArrayList<MaterialBodyVM> systems = new ArrayList<>();
        HttpURLConnection conn = null;
        BufferedReader br = null;
        Gson gson = new Gson();

        String sUrl = URL_BASE + matToFind + "?refsystem=" + curSystemName;


        Log.d("URL string created: ", sUrl);

        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            InputStream is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            systems = gson.fromJson(br, new TypeToken<ArrayList<MaterialBodyVM>>(){}.getType());
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
