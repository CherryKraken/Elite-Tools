package com.connorboyle.elitetools.asynctasks;

import android.os.AsyncTask;

/**
 * Created by Connor Boyle on 21-Oct-17.
 */

public abstract class ParseJsonTask<A, B, C> extends AsyncTask<A, B, C> {
    protected final OnTaskCompleteHelper caller;
    private final OnTaskCompleteHelper.Task task;

    public ParseJsonTask(OnTaskCompleteHelper caller, OnTaskCompleteHelper.Task task) {
        this.caller = caller;
        this.task = task;
    }

    @Override
    protected void onPostExecute(C c) {
        caller.onAsyncTaskComplete(task, c);
    }
}
