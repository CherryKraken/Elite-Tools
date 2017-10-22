package com.connorboyle.elitetools.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetSystemsLiteTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 21-Oct-17.
 */

public class HomeView extends Fragment implements OnTaskCompleteHelper {
    private View v;
    private AutoCompleteTextView etCurSystem;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_layout, container, false);
        setupControls();
        new GetSystemsLiteTask(this).execute();
        return v;
    }

    private void setupControls() {
        etCurSystem = (AutoCompleteTextView) v.findViewById(R.id.etHomeCurSystem);
    }

    @Override
    public void onAsyncTaskComplete(Task task, Object obj) {
        if (task == Task.SYSTEMS_LITE && obj != null && obj instanceof ArrayList<?>) {
            etCurSystem.setAdapter(new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, (ArrayList<?>)obj));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
