package com.cragchat.mobile.ui.contract;

import com.cragchat.mobile.ui.model.Area;

import io.reactivex.Observer;

/**
 * Created by timde on 2/14/2018.
 */

public interface AreaContract {

    interface AreaView {
        void present(Area area);
    }

    interface AreaPresenter{
        void updateArea(String areaKey);
    }
}
