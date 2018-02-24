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
import com.connorboyle.elitetools.models.MaterialSystemVM;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Connor Boyle on 23-Feb-18.
 */

public class MaterialSystemsAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<MaterialSystemVM> list;

    public MaterialSystemsAdapter(@NonNull Context context, @NonNull ArrayList<MaterialSystemVM> list) {
        super(context, 0, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.simple_row_view, parent,false);

        MaterialSystemVM mat = this.list.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.tvSimpleRowTitle);
        name.setText(String.format(Locale.getDefault(), "%s", mat.systemName));

        TextView desc = (TextView) listItem.findViewById(R.id.tvSimpleRowDesc);
        desc.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        desc.setText(String.format(Locale.getDefault(), "%.2f LY", mat.systemDistance));

        return listItem;
    }
}
