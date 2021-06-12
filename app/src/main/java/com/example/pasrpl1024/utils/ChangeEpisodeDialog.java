package com.example.pasrpl1024.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pasrpl1024.MainActivity;
import com.example.pasrpl1024.R;

public class ChangeEpisodeDialog extends DialogFragment {

    public Dialog onCreateDialog(@NonNull  Bundle savedInstanceState, Activity activity, AnimeAdapter.AdapterClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_1, null);
        builder.setView(view)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    String string = ((EditText) view.findViewById(R.id.dialog_1_textedit)).getText().toString();
                    if (string == null || string.isEmpty()) {
                        Toast.makeText(activity, "Episode must not empty!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        new ChangeEpisodeDialog().onCreateDialog(new Bundle(), activity, clickListener).show();
                        return;
                    }
                    if (!string.matches("[0-9]+")) {
                        Toast.makeText(activity, "Must use number!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        new ChangeEpisodeDialog().onCreateDialog(new Bundle(), activity, clickListener).show();
                        return;
                    }
                    int number = Integer.parseInt(string);
                    if (!clickListener.click(number)) {
                        Toast.makeText(activity, "Watched must not exceed current episode!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        new ChangeEpisodeDialog().onCreateDialog(new Bundle(), activity, clickListener).show();
                    }
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
        return builder.create();
    }

}
