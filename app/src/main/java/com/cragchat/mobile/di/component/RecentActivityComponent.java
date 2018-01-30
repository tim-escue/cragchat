package com.cragchat.mobile.di.component;

import com.cragchat.mobile.CragChatApplication;
import com.cragchat.mobile.di.module.ApplicationModule;
import com.cragchat.mobile.di.module.AuthenticationModule;
import com.cragchat.mobile.di.module.RecentActivityModule;
import com.cragchat.mobile.di.module.RepositoryModule;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.fragments.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by timde on 1/30/2018.
 */

@Component(modules = {ApplicationModule.class, RepositoryModule.class, RecentActivityModule.class})
public interface RecentActivityComponent {

    void inject(RecentActivityComponent fragment);

}