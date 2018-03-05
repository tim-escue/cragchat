package com.cragchat.mobile.mvp.view.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class NotificationDialog extends DialogFragment {

    public static NotificationDialog newInstance(String message) {
        NotificationDialog f = new NotificationDialog();
        Bundle b = new Bundle();
        b.putString("message", message);
        f.setArguments(b);
        return f;
    }

    public static NotificationDialog newInstance(String message, Class open) {
        NotificationDialog f = new NotificationDialog();
        Bundle b = new Bundle();
        b.putString("class", open.getName());
        b.putString("message", message);
        f.setArguments(b);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getArguments().getString("message"))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String c = getArguments().getString("class");
                        if (c != null) {
                            try {
                                //Log.d("CLAASNAME:" + c + ":");
                                Class clazz = Class.forName(c);
                                Intent next = new Intent(getActivity(), clazz);
                                getActivity().startActivity(next);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}