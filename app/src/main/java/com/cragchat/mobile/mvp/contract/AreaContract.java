package com.cragchat.mobile.mvp.contract;

import com.cragchat.mobile.domain.model.Area;

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
