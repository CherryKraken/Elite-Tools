package com.connorboyle.elitetools.asynctasks;

/**
 * Created by Connor Boyle on 21-Oct-17.
 */

public interface OnTaskCompleteHelper {
    public enum Task {
        SYSTEMS_LITE,
        SYSTEMS_FULL,
        SYSTEM_INFO,
        SYSTEM_COORDS,
        RECIPE,
        INGREDIENTS,
        INGREDIENT_INFO,
        GRADES,
        MODIFICATIONS,
        MODULES,
        ENGINEERS,
        ENGINEERS_FOR_GRADE,
        ENGINEER_INFO
    }

    /**
     * Called when an AsyncTask has completed successfully
     * @param task The task of task completed
     * @param obj The data that was returned
     */
    public void onAsyncTaskComplete(Task task, Object obj);
}
