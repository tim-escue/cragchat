package com.cragchat.mobile.di.module;

import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.util.NavigationUtil;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by timde on 2/5/2018.
 */

@Module
public class AreaModule {

    @Provides
    Area provideArea(AreaActivity activity) {
        return activity.getIntent().getParcelableExtra(NavigationUtil.ENTITY);
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

}
