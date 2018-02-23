package com.connorboyle.elitetools.asynctasks;

import android.support.v4.app.Fragment;

import com.connorboyle.elitetools.models.System;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Connor Boyle on 22-Oct-17.
 */

public class GetSystemCoordsTask extends ParseJsonTask<String, Void, System> {
    public GetSystemCoordsTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.SYSTEM_COORDS);
    }

    @Override
    protected System doInBackground(String... params) {
        System system = new System("Sol", 0, 0, 0);

        if (params.length != 0) {
            system.name = params[0];
        }

        try {
            InputStream is = ((Fragment)caller).getContext().getAssets().open("system-coords-keyed.json");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            JsonReader jr = new JsonReader(br);
            jr.beginObject();

            while (jr.hasNext()) {
                if (jr.nextName().equalsIgnoreCase(params[0])){
                    jr.beginObject();
                    jr.nextName();
                    system.x = jr.nextDouble();
                    jr.nextName();
                    system.y = jr.nextDouble();
                    jr.nextName();
                    system.z = jr.nextDouble();
                    jr.endObject();

                    jr.close(); // also closes BufferedReader
                    is.close();
                    return system;
                } else {
                    jr.skipValue();
                }
            }

            jr.endObject();
            jr.close(); // also closes BufferedReader
            is.close();
        } catch(IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        return system;
    }
}
