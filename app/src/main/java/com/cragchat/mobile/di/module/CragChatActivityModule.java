package com.cragchat.mobile.di.module;

import dagger.Module;
import dagger.Provides;

@Module
public class CragChatActivityModule {

    /*@Provides
    MainView provideMainView(MainActivity mainActivity){
        return mainActivity;
    }

    @Provides
    MainPresenter provideMainPresenter(MainView mainView, ApiService apiService){
        return new MainPresenterImpl(mainView, apiService);
    }*/

    @Provides
    String provideMessage() {
        return "09f0eafea";
    }
}