package com.cragchat.mobile.di.module;

import android.content.Context;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.FragmentScoped;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.presenter.RecentActivityFragmentPresenter;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.fragments.CragsFragment;
import com.cragchat.mobile.ui.view.fragments.RecentActivityFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import static com.cragchat.mobile.util.NavigationUtil.ENTITY_KEY;

/**
 * Created by timde on 1/30/2018.
 */

@Module(includes = {ApplicationModule.class})
public abstract class RecentActivityModule {



    @Provides
    Area provideArea(RecentActivityFragment fragment) {
        return fragment.getArguments().getParcelable(ENTITY_KEY);
    }

    @Provides
    String providesAreaKey(Area area) {
        return area.getKey();
    }

}
