package com.cragchat.mobile.ui.presenter;

import com.cragchat.mobile.ui.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.view.fragments.AreaListFragment;

import javax.inject.Inject;

/**
 * Created by timde on 1/4/2018.
 */

public class AreaActivityPresenter  {



    private AreaActivity areaActivity;

    @Inject
    public AreaActivityPresenter(AreaActivity activity, AreaListFragment areaFragment,
                                 RecentActivityFragment recentActivityFragment, Area area) {
        this.areaActivity = activity;

    }


}
