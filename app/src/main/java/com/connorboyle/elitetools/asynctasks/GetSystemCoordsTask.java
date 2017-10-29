package com.connorboyle.elitetools.asynctasks;

import com.connorboyle.elitetools.classes.System;

/**
 * Created by Connor Boyle on 22-Oct-17.
 */

public class GetSystemCoordsTask extends ParseJsonTask<String, Void, System> {
    public GetSystemCoordsTask(OnTaskCompleteHelper caller) {
        super(caller, OnTaskCompleteHelper.Task.SYSTEM_COORDS);
    }

    @Override
    protected System doInBackground(String... params) {
        return null;
    }
}
