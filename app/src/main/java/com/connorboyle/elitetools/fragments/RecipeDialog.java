package com.connorboyle.elitetools.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.connorboyle.elitetools.adapters.EffectsHashMapAdapter;
import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.classes.Recipe;

import java.util.ArrayList;

/**
 * Created by Connor Boyle on 24-Sep-17.
 */

public class RecipeDialog extends DialogFragment {
    View v;
    TextView tvBPTitle;
    ListView lvIngredients, lvEffects, lvEngineers;

    Recipe mRecipe;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mRecipe = (Recipe) getArguments().getSerializable("recipe");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.recipe_view, null);
        setView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().cancel();
                    }
                })
        ;

        return builder.create();
    }

    private void setView() {
        tvBPTitle = (TextView) v.findViewById(R.id.tvBPTitle);
        lvEffects = (ListView) v.findViewById(R.id.lvEffects);
        lvIngredients = (ListView) v.findViewById(R.id.lvIngredients);
        lvEngineers = (ListView) v.findViewById(R.id.lvEngineers);

        tvBPTitle.setText(mRecipe.title);
        lvEffects.setAdapter(new EffectsHashMapAdapter(mRecipe.effects));
        lvIngredients.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_row_view, R.id.tvSimpleRowTitle, mRecipe.ingredients));
        lvEngineers.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_row_view, R.id.tvSimpleRowTitle, new ArrayList<>(mRecipe.engineers.values())));

    }
}
