package com.cragchat.mobile.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.adapters.recycler.CommentRecyclerAdapter;
import com.cragchat.mobile.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.comments.CommentManager;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.sql.SendCommentTask;
import com.cragchat.mobile.user.User;

public class CommentSectionFragment extends Fragment implements View.OnClickListener {

    private CommentManager manager;
    private CommentRecyclerAdapter adapter;

    public static CommentSectionFragment newInstance(int displayId, String table) {
        CommentSectionFragment f = new CommentSectionFragment();
        Bundle b = new Bundle();
        b.putString("display_id", String.valueOf(displayId));
        b.putString("table", table);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_comment_section, container, false);

        manager = new CommentManager();

        for (Comment i : LocalDatabase.getInstance(getContext()).getCommentsForTable(Integer.parseInt(getArguments().getString("display_id")), getArguments().getString("table"))) {
            manager.addComment(i);
        }
        manager.sortByScore();


        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_comment_sort);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_comment_sort_options, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(listener);

        adapter = new CommentRecyclerAdapter(getActivity(), manager, getArguments().getString("table"));
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
        RecyclerUtils.setAdapterAndManager(recList, adapter, LinearLayoutManager.VERTICAL);

       if (manager.getCommentList().size() == 0) {
           TextView empty = (TextView) view.findViewById(R.id.list_empty);
           empty.setVisibility(View.VISIBLE);
       }

        return view;
    }

    AlertDialog getCommentDialog(final EditText txtUrl, final View view, final CommentRecyclerAdapter adapter) {
        AlertDialog dialog = null;
        dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Add Comment")
                .setView(txtUrl)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = txtUrl.getText().toString();
                        if (!url.trim().equals("")) {
                            //  public static String insertComment(String table, String author, String text, String display_id, String parent, String depth) {
                            if (((CragChatActivity) getActivity()).hasConnection()) {
                                new SendCommentTask(getActivity(), adapter, getArguments().getString("table")).execute(getArguments().getString("table"), User.currentToken(getActivity()), url, getArguments().getString("display_id"),
                                        "-1", "0");
                            } else {
                                String store = "COMMENT###" + getArguments().getString("table") + "###" + User.currentToken(getActivity()) + "###" + url + "###" + getArguments().getString("display_id") + "###" +
                                        "-1###0";
                                LocalDatabase.getInstance(getContext()).store(getActivity(), store);
                                Toast.makeText(getContext(), "Unable to post comment - will try again later", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Can't add empty comment", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog dialog1 = new AlertDialog.Builder(view.getContext())
                                .setMessage("Are you sure you want to cancel this comment?")
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final EditText newt = new EditText(getContext());
                                        newt.setText(txtUrl.getText());
                                        getCommentDialog(newt, view, adapter).show();
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

    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.spinner_comment_sort) {
                String option = parent.getItemAtPosition(position).toString();
                if (option.equals("Score")) {
                    manager.sortByScore();
                } else if (option.equals("Date")) {
                    manager.sortByDate();
                }
                RecyclerView lv = (RecyclerView) getView().findViewById(R.id.comment_section_list);
                lv.getAdapter().notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (User.currentToken(getActivity()) != null) {

                final EditText txtUrl = new EditText(getActivity());

                txtUrl.setHint("Type your comment here.");
                AlertDialog dialog = getCommentDialog(txtUrl, view, adapter);
                dialog.show();
            } else {
                Toast.makeText(getContext(), "Must be logged in to addto a comment", Toast.LENGTH_LONG).show();
            }
        }
    }
}
