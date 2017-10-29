package com.connorboyle.elitetools.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.asynctasks.GetSystemsLiteTask;
import com.connorboyle.elitetools.asynctasks.OnTaskCompleteHelper;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 21-Oct-17.
 */

public class HomeView extends Fragment implements OnTaskCompleteHelper, View.OnClickListener {
    private View v;
    private AutoCompleteTextView etHomeCurSystem;
    private Button btnSaveSystem, btnDiscord, btnGithub;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_layout, container, false);
        setupControls();
        new GetSystemsLiteTask(this).execute();

        // Restore current system
        SharedPreferences settings = getActivity().getSharedPreferences(getActivity()
                .getString(R.string.curr_system_setting), 0);
        etHomeCurSystem.setText(settings.getString(getActivity()
                .getString(R.string.curr_system_setting), ""));

        return v;
    }

    private void setupControls() {
        etHomeCurSystem = (AutoCompleteTextView) v.findViewById(R.id.etHomeCurSystem);
        btnSaveSystem = (Button) v.findViewById(R.id.btnSaveSystem);
        btnDiscord = (Button) v.findViewById(R.id.btnDiscord);
        btnGithub = (Button) v.findViewById(R.id.btnGithub);

        btnSaveSystem.setOnClickListener(this);
        btnDiscord.setOnClickListener(this);
        btnGithub.setOnClickListener(this);
    }

    @Override
    public void onAsyncTaskComplete(Task task, Object obj) {
        if (task == Task.SYSTEMS_LITE && obj != null && obj instanceof ArrayList<?>) {
            etHomeCurSystem.setAdapter(new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, (ArrayList<?>)obj));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor edt = getActivity().getSharedPreferences(getActivity()
                .getString(R.string.curr_system_setting), 0).edit();
        edt.putString(getActivity().getString(R.string.curr_system_setting),
                etHomeCurSystem.getText().toString());
        edt.apply();
    }

    @Override
    public void onClick(View v) {
        Intent browserIntent;
        switch (v.getId()) {
            case R.id.btnDiscord: // Linking to Discord profile's currently unavailable
//                browserIntent = new Intent(
//                        Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/r/elitedangerous/"));
//                startActivity(browserIntent);
                break;
            case R.id.btnGithub:
                browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/CherryKraken/Elite-Tools"));
                startActivity(browserIntent);
                break;

            case R.id.btnSaveSystem:
                if (etHomeCurSystem.getText().toString().trim().length() > 1) {
                    ListAdapter la = etHomeCurSystem.getAdapter();
                    for (int i = 0; i < la.getCount(); i++) {
                        String str = la.getItem(i).toString();
                        if (etHomeCurSystem.getText().toString().toUpperCase().equals(str)) {
                            return;
                        }
                    }
                    Toast.makeText(getContext(), "System entered does not exist", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
