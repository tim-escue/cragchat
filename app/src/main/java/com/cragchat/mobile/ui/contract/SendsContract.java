package com.cragchat.mobile.ui.contract;

import com.cragchat.mobile.ui.model.Send;

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
