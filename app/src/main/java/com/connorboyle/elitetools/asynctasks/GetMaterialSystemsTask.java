package com.connorboyle.elitetools.asynctasks;

import android.util.Log;

import com.connorboyle.elitetools.models.MaterialBodyVM;
import com.connorboyle.elitetools.models.MaterialSystemVM;
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

public class GetMaterialSystemsTask extends ParseJsonTask<Void, Void, ArrayList<MaterialSystemVM>> {
    private static final String URL_BASE = "http://138.197.151.119/api/v1/materials/";
    private static final String AND = "&";

    private final String curSystemName;
    private final String matToFind;

    public GetMaterialSystemsTask(OnTaskCompleteHelper caller, String matToFind, String curSystemName) {
        super(caller, OnTaskCompleteHelper.Task.MATERIAL_SYSTEMS);
        this.curSystemName = curSystemName;
        this.matToFind = matToFind;
    }

    @Override
    protected ArrayList<MaterialSystemVM> doInBackground(Void... voids) {
        ArrayList<MaterialSystemVM> systems = new ArrayList<>();
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

            systems = gson.fromJson(br, new TypeToken<ArrayList<MaterialSystemVM>>(){}.getType());
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
