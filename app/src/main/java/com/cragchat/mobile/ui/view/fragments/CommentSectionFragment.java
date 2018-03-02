package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Comment;
import com.cragchat.mobile.ui.view.adapters.recycler.CommentRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by timde on 10/20/2017.
 */

public class CommentSectionFragment extends DaggerFragment implements View.OnClickListener {

    public static final String TABLE_LOCATION = "Location";
    public static final String TABLE_DISCUSSION = "Discussion";
    public static final String TABLE_BETA = "Beta";

    @BindView(R.id.spinner_comment_sort)
    Spinner spinner;

    @BindView(R.id.comment_section_list)
    RecyclerView recyclerView;

    @BindView(R.id.list_empty)
    TextView empty;

    private CommentRecyclerAdapter adapter;

    @Inject
    @Named(InjectionNames.ENTITY_KEY)
    String mEntityKey;

    @Inject
    Repository mRepository;

    @Inject
    Authentication mAuthentication;

    private String table;

    @Inject
    public CommentSectionFragment() {
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_comment_section, container, false);
        ButterKnife.bind(this, view);

        adapter = new CommentRecyclerAdapter(getContext(), null, table, mEntityKey, mRepository, mAuthentication);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
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

        mRepository.observeComment(mEntityKey, table).subscribe(this::present);

        return view;
    }



        public void update(Comment comment) {
            adapter.updateSingle(comment);
            showViews();
        }

        private void showViews() {
            empty.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        public void present(List<Comment> comments) {
            if (comments.isEmpty()) {
                empty.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            } else {
                showViews();

                adapter.update(comments);
            }
        }




    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (mAuthentication.isLoggedIn(view.getContext())) {
                Dialog.getAddCommentDialog(mAuthentication, mRepository, null, view.getContext(),
                        mEntityKey, table, null, this::update).show();
            } else {
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to add a comment.");
                df.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        }

    }
}
