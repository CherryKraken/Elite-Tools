package com.connorboyle.elitetools.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.adapters.HashMapAdapter;

import java.util.HashMap;

/**
 * Created by Connor Boyle on 27-Sep-17.
 */

public class EffectsHashMapAdapter extends HashMapAdapter {
    public EffectsHashMapAdapter(HashMap map) {
        super(map);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView != null ? convertView
                : LayoutInflater.from(parent.getContext())
                .inflate(R.layout.effects_row_view, parent, false);

        TextView tvEffect = (TextView) result.findViewById(R.id.tvRowEffectName);
        TextView tvMin = (TextView) result.findViewById(R.id.tvRowEffectMin);
        TextView tvMax = (TextView) result.findViewById(R.id.tvRowEffectMax);

        double min = ((double[]) getValueAt(position))[0];
        double max = ((double[]) getValueAt(position))[1];
        String sMin, sMax, name = (String) getKeyAt(position);

        // Check if values are exact integers (i.e. effect values are a range of percentages)
        if (min == Math.floor(min) && max == Math.floor(max)) {
            sMin = String.format("%d%%", (int) min);
            sMax = String.format("%d%%", (int) max);
        } else {
            sMin = String.valueOf(min);
            sMax = String.valueOf(max);
        }

        tvEffect.setText(name);
        tvMin.setText(sMin);
        tvMax.setText(sMax);

        if (min < max) {
            if (0 < max && 0 < min) {
                tvMin.setTextColor(Color.CYAN);
                tvMax.setTextColor(Color.CYAN);
            } else if (min < 0 && max < 0) {
                tvMin.setTextColor(Color.RED);
                tvMax.setTextColor(Color.RED);
            } else {
                tvMin.setTextColor(Color.RED);
                tvMax.setTextColor(Color.CYAN);
            }
        } else if (min > max) {
            if (0 < max && 0 < min) {
                tvMin.setTextColor(Color.RED);
                tvMax.setTextColor(Color.RED);
            } else if (min < 0 && max < 0) {
                tvMin.setTextColor(Color.CYAN);
                tvMax.setTextColor(Color.CYAN);
            } else {
                tvMin.setTextColor(Color.RED);
                tvMax.setTextColor(Color.CYAN);
            }
        } else {
            tvMin.setTextColor(Color.CYAN);
            tvMax.setTextColor(Color.CYAN);
        }

        if (max == 0) {
            tvMax.setTextColor(Color.parseColor("#ff8100"));
        }
        if (min == 0) {
            tvMin.setTextColor(Color.parseColor("#ff8100"));
        }

        return result;
    }
}
