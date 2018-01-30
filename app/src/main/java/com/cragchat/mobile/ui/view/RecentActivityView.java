package com.cragchat.mobile.ui.view;

import com.cragchat.mobile.ui.model.Datable;

import java.util.List;

/**
 * Created by timde on 1/30/2018.
 */

public interface RecentActivityView {

    void showList(List<Datable> datables);

    void hideList();

    void hideProgressBar();
}
