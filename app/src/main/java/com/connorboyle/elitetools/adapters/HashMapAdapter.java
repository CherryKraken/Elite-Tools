package com.connorboyle.elitetools.adapters;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Connor Boyle on 27-Sep-17.
 */

public abstract class HashMapAdapter<K, V> extends BaseAdapter {
    private ArrayList<Map.Entry<K, V>> mEntries;

    public HashMapAdapter(HashMap<K, V> map) {
        mEntries = new ArrayList<>();
        mEntries.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Map.Entry<K, V> getItem(int position) {
        return mEntries.get(position);
    }

    public K getKeyAt(int position) {
        return getItem(position).getKey();
    }

    public V getValueAt(int position) {
        return getItem(position).getValue();
    }
}
