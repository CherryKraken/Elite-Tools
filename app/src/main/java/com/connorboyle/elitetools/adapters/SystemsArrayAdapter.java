package com.connorboyle.elitetools.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.models.System;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Connor Boyle on 22-Feb-18.
 */

public class SystemsArrayAdapter extends ArrayAdapter {
    private final Context context;
    private final System refSystem;
    private final ArrayList<System> systems;

    public SystemsArrayAdapter(@NonNull Context context, ArrayList<System> systems, System refSystem) {
        super(context, 0, systems);
        this.context = context;
        this.systems = systems;
        this.refSystem = refSystem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.simple_row_view, parent,false);

        System ss = this.systems.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.tvSimpleRowTitle);
        name.setText(ss.name);

        TextView desc = (TextView) listItem.findViewById(R.id.tvSimpleRowDesc);
        desc.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        desc.setText(String.format(Locale.getDefault(), "%.2f LY", ss.distanceTo(this.refSystem)));

        return listItem;
    }
}
