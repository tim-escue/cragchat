package com.cragchat.mobile.mvp.contract;

import com.cragchat.mobile.domain.model.Send;

import java.util.List;

/**
 * Created by timde on 2/18/2018.
 */

public interface SendsContract {

    interface SendView {
        void show(List<Send> sends);
    }

    interface Presenter {
        void loadSends(String entityKey);
    }
}
