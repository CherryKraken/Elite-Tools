package com.connorboyle.elitetools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Connor Boyle on 24-Sep-17.
 */

public class RecipeDialog extends DialogFragment {
    View v;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Recipe recipe = (Recipe) getArguments().getSerializable("recipe");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.recipe_view, null);
        setView(recipe);
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

    private void setView(Recipe recipe) {
    }
}
