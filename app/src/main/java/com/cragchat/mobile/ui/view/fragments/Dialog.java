package com.cragchat.mobile.ui.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Comment;

/**
 * Created by timde on 12/21/2017.
 */

public class Dialog {

    public static AlertDialog getAddCommentDialog(
            final String currentText, final Context context,
            final String entityId, final String table,
            final Comment commentToEdit,
            final Callback<Comment> callback) {
        final EditText editText = new EditText(context);
        if (currentText != null && !currentText.isEmpty()) {
            editText.setText(currentText);
        }
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Add Comment")
                .setView(editText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String comment = editText.getText().toString().trim();
                        if (!comment.isEmpty()) {
                            String token = Authentication.getAuthenticatedUser(context).getToken();
                            if (commentToEdit != null) {
                                Repository.editComment(token, comment,
                                        commentToEdit.getKey(), callback);
                            } else {
                                Repository.addComment(token, comment,
                                        entityId, table, callback);
                            }

                        } else {
                            Toast.makeText(context, "Can't add empty comment", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog dialog1 = new AlertDialog.Builder(context)
                                .setMessage("Are you sure you want to cancel this comment?")
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        getAddCommentDialog(editText.getText().toString(),
                                                context, entityId, table, commentToEdit, callback)
                                                .show();
                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                .create();
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.show();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static AlertDialog getReplyCommentDialog(
            String currentText, final Context context,
            final String entityId, final String table,
            final String parentId, final int depth,
            final Callback<Comment> callback) {
        final EditText editText = new EditText(context);
        editText.setHint("Enter comment here");
        if (currentText != null && !currentText.isEmpty()) {
            editText.setText(currentText);
        }
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Add Comment")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String comment = editText.getText().toString().trim();
                        if (!comment.isEmpty()) {
                            String token = Authentication.getAuthenticatedUser(context).getToken();
                            Repository.replyToComment(token, comment, entityId, table, parentId,
                                    depth, callback
                            );
                        } else {
                            Toast.makeText(context, "Can't add empty comment", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog dialog1 = new AlertDialog.Builder(context)
                                .setMessage("Are you sure you want to cancel this comment?")
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        getReplyCommentDialog(editText.getText().toString(),
                                                context, entityId, table, parentId, depth, callback).show();
                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                .create();
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.show();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
