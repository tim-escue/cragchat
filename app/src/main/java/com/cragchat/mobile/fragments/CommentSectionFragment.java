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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.model.realm.RealmComment;
import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.remote.ErrorHandlingObserverable;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;
import com.cragchat.mobile.view.adapters.recycler.NewCommentRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by timde on 10/20/2017.
 */

public class CommentSectionFragment extends Fragment implements View.OnClickListener {

    private NewCommentRecyclerAdapter adapter;
    private Realm mRealm;
    private String mEntityId;
    private String table;

    public static CommentSectionFragment newInstance(String entityId, String table) {
        CommentSectionFragment f = new CommentSectionFragment();
        Bundle b = new Bundle();
        b.putString("entityId", entityId);
        b.putString("table", table);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_comment_section, container, false);

        mEntityId = getArguments().getString("entityId");
        table = getArguments().getString("table");

        mRealm = Realm.getDefaultInstance();

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_comment_sort);
        final ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_comment_sort_options, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        //spinner.setOnItemSelectedListener(listener);

        final List<RealmComment> comments = mRealm.copyFromRealm(mRealm.where(RealmComment.class)
                .equalTo(RealmComment.FIELD_ENTITY_ID, mEntityId)
                .equalTo(RealmComment.FIELD_TABLE, table)
                .findAll());
        adapter = new NewCommentRecyclerAdapter(getContext(), comments, table, mEntityId);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
        RecyclerUtils.setAdapterAndManager(recList, adapter, LinearLayoutManager.VERTICAL);

        if (Network.isConnected(getContext())) {
            Repository.getComments(mEntityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorHandlingObserverable<List<RealmComment>>() {
                        @Override
                        public void onSuccess(final List<RealmComment> realmComments) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(realmComments);
                                }
                            });
                            final List<RealmComment> copied = realm.copyFromRealm(realm.where(RealmComment.class)
                                    .equalTo(RealmComment.FIELD_ENTITY_ID, mEntityId)
                                    .equalTo(RealmComment.FIELD_TABLE, table)
                                    .findAll());
                            realm.close();
                            adapter.update(copied);
                        }
                    });
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (Authentication.isLoggedIn(getContext())) {
                final EditText txtUrl = new EditText(getContext());
                AlertDialog dialog = getAddCommentDialog(adapter, null, getContext(), mEntityId, table, null);
                dialog.show();
            } else {
                Toast.makeText(getContext(), "Must be logged in to add a comment", Toast.LENGTH_LONG).show();
            }
        }

    }

    public static AlertDialog getAddCommentDialog(final NewCommentRecyclerAdapter adapter,
                                                  String currentText, final Context context,
                                                  final String entityId, final String table,
                                                  final Comment commentToEdit) {
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
                            if (Network.isConnected(context)) {
                                String token = Authentication.getAuthenticatedUser(context).getToken();
                                Observable<RealmComment> call;
                                if (commentToEdit != null) {
                                    call = RetroFitRestApi.getInstance().postCommentEdit(
                                            token,
                                            comment,
                                            commentToEdit.getKey()
                                    );
                                } else {
                                    call = RetroFitRestApi.getInstance().postComment(
                                            token,
                                            comment,
                                            entityId,
                                            table
                                    );
                                    Repository.addComment(token, comment, entityId, table);
                                    //todo: register this fragment as obserer to addcomment
                                }
                                call.subscribeOn(Schedulers.io()).
                                        observeOn(AndroidSchedulers.mainThread()).
                                        subscribe(new ErrorHandlingObserverable<RealmComment>() {
                                            @Override
                                            public void onSuccess(final RealmComment realmComment) {
                                                Realm realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        realm.insertOrUpdate(realmComment);
                                                    }
                                                });
                                                final List<RealmComment> copied = realm.copyFromRealm(realm.where(RealmComment.class)
                                                        .equalTo(RealmComment.FIELD_ENTITY_ID, entityId)
                                                        .equalTo(RealmComment.FIELD_TABLE, table)
                                                        .findAll());
                                                realm.close();
                                                adapter.update(copied);
                                            }
                                        });
                            } else {
                                Toast.makeText(context,
                                        "Cannot post comment while offline - queuing to add when connection is re-established",
                                        Toast.LENGTH_LONG).show();
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
                                        getAddCommentDialog(adapter, editText.getText().toString(), context, entityId, table, commentToEdit).show();
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

    public static AlertDialog getReplyCommentDialog(final NewCommentRecyclerAdapter adapter,
                                                    String currentText, final Context context,
                                                    final String entityId, final String table,
                                                    final String parentId, final int depth) {
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
                            if (Network.isConnected(context)) {
                                String token = Authentication.getAuthenticatedUser(context).getToken();
                                RetroFitRestApi.getInstance().postCommentReply(
                                        token,
                                        comment,
                                        entityId,
                                        table,
                                        parentId,
                                        depth
                                )
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<RealmComment>() {
                                            @Override
                                            public void accept(final RealmComment realmComment) throws Exception {
                                                Realm realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        realm.insertOrUpdate(realmComment);
                                                    }
                                                });
                                                final List<RealmComment> copied = realm.copyFromRealm(realm.where(RealmComment.class)
                                                        .equalTo(RealmComment.FIELD_ENTITY_ID, entityId)
                                                        .equalTo(RealmComment.FIELD_TABLE, table)
                                                        .findAll());
                                                realm.close();
                                                adapter.update(copied);
                                            }
                                        });

                            } else {
                                Toast.makeText(context,
                                        "Cannot post comment while offline - queuing to add when connection is re-established",
                                        Toast.LENGTH_LONG).show();
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
                                        getReplyCommentDialog(adapter, editText.getText().toString(), context, entityId, table, parentId, depth).show();
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
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
