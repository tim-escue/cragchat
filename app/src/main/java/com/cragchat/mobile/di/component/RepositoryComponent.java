package com.cragchat.mobile.di.component;

import com.cragchat.mobile.CragChatApplication;
import com.cragchat.mobile.di.module.ApplicationModule;
import com.cragchat.mobile.di.module.RepositoryModule;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.ui.view.fragments.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by timde on 1/24/2018.
 */
@Singleton
@Component(modules = {ApplicationModule.class, RepositoryModule.class})
public interface RepositoryComponent {
    void inject(CragChatApplication application);

    void inject(CragChatActivity activity);

    void inject(BaseFragment fragment);
}
