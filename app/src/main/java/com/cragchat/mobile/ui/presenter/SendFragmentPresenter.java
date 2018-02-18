package com.cragchat.mobile.ui.presenter;

import android.arch.lifecycle.Lifecycle;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.contract.SendsContract;
import com.cragchat.mobile.ui.model.Send;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.ui.view.adapters.recycler.SendRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendFragmentPresenter {

    private SendsContract.SendView mView;
    private Repository mRepository;

    public SendFragmentPresenter(SendsContract.SendView view, Repository repository) {
        this.mView = view;
        this.mRepository = repository;
    }

    public void loadSends(String entityKey) {
        List<Send> sends = mRepository.getSends(entityKey, new Callback<List<Send>>() {
            @Override
            public void onSuccess(List<Send> object) {
                mView.show(object);
            }

            @Override
            public void onFailure() {

            }
        });
        mView.show(sends);
    }
}