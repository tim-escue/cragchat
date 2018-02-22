package com.cragchat.mobile.di.module;

import com.cragchat.mobile.di.ActivityScoped;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.ui.contract.AreaContract;
import com.cragchat.mobile.ui.presenter.AreaActivityPresenter;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.util.NavigationUtil;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by timde on 2/5/2018.
 */

@Module
public class AreaModule {

    @ActivityScoped
    @Provides
    Area provideArea(AreaActivity activity, CragChatDatabase database) {
        return database.getArea(activity.getIntent().getStringExtra(NavigationUtil.ENTITY_KEY));
    }

    @Provides
    @Named(InjectionNames.ENTITY_KEY)
    String entityKey(Area area) {
        return area.getKey();
    }

    @Provides
    @Named(InjectionNames.AREA_IDS)
    String[] areaIds(Area area) {
        return area.getSubAreas().toArray(new String[area.getSubAreas().size()]);
    }

    @Provides
    @Named(InjectionNames.ROUTE_IDS)
    String[] routeIds(Area area) {
        return area.getRoutes().toArray(new String[area.getRoutes().size()]);
    }

    @Provides
    List<String> subAreas(Area area) {
        return area.getSubAreas();
    }

    @Provides
    AreaContract.AreaPresenter presenter(AreaActivity areaActivity, Repository repository) {
        return new AreaActivityPresenter(areaActivity, repository);
    }

}
