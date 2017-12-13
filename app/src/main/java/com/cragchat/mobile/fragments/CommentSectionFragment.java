package com.cragchat.mobile.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.CommentRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import java.util.List;

/**
 * Created by timde on 10/20/2017.
 */

public class CommentSectionFragment extends Fragment implements View.OnClickListener {

    private CommentRecyclerAdapter adapter;
    private String mEntityId;
    private String table;
    private Callback<List<Comment>> commentsCallback = new Callback<List<Comment>>() {
        @Override
        public void onSuccess(List<Comment> objects) {
            adapter.update(objects);
        }

        @Override
        public void onFailure() {

        }
    };
    private Callback<Comment> newCommentCallback = new Callback<Comment>() {
        @Override
        public void onSuccess(Comment objects) {
            adapter.updateSingle(objects);
        }

        @Override
        public void onFailure() {

        }
    };

    public static CommentSectionFragment newInstance(String entityId, String table) {
        CommentSectionFragment f = new CommentSectionFragment();
        Bundle b = new Bundle();
        b.putString("entityId", entityId);
        b.putString("table", table);
        f.setArguments(b);
        return f;
    }

    public static AlertDialog getAddCommentDialog(final CommentRecyclerAdapter adapter,
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
                                        getAddCommentDialog(adapter, editText.getText().toString(),
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

    public static AlertDialog getReplyCommentDialog(final CommentRecyclerAdapter adapter,
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
                            Repository.replyToComment(
                                    token,
                                    comment,
                                    entityId,
                                    table,
                                    parentId,
                                    depth,
                                    callback
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
                                        getReplyCommentDialog(adapter, editText.getText().toString(),
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_comment_section, container, false);

        mEntityId = getArguments().getString("entityId");
        table = getArguments().getString("table");


        adapter = new CommentRecyclerAdapter(getContext(),
                Repository.getComments(mEntityId, table, commentsCallback), table, mEntityId);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
        RecyclerUtils.setAdapterAndManager(recList, adapter, LinearLayoutManager.VERTICAL);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_comment_sort);
        final ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_comment_sort_options, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.sort(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (Authentication.isLoggedIn(getContext())) {
                getAddCommentDialog(adapter, null, getContext(),
                        mEntityId, table, null, newCommentCallback).show();
            } else {
                Toast.makeText(getContext(),
                        "Must be logged in to add a comment", Toast.LENGTH_LONG).show();
            }
        }

    }
}
