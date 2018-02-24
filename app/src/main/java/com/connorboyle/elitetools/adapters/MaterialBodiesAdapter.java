package com.connorboyle.elitetools.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.models.MaterialBodyVM;
import com.connorboyle.elitetools.models.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Connor Boyle on 23-Feb-18.
 */

public class MaterialBodiesAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<MaterialBodyVM> list;

    public MaterialBodiesAdapter(@NonNull Context context, @NonNull ArrayList<MaterialBodyVM> list) {
        super(context, 0, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.extended_row_view, parent,false);

        MaterialBodyVM mat = this.list.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.tvExtRowTitle);
        name.setText(String.format(Locale.getDefault(), "%s\n   %s\n          %.2f%%", mat.systemName, mat.bodyName, mat.concentration));

        TextView desc = (TextView) listItem.findViewById(R.id.tvExtRowDist);
        desc.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        desc.setText(String.format(Locale.getDefault(), "%.2f LY\n~ %d LS", mat.systemDistance, (int) mat.distanceToArrival));

        return listItem;
    }
}
