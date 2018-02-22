package com.cragchat.mobile.ui.contract;

import java.util.List;

/**
 * Created by timde on 2/14/2018.
 */

public interface SearchContract {

    interface SearchView {
        void show(List list);
    }

    interface SearchPresenter {
        void loadSearchResults(String query);
    }
}
