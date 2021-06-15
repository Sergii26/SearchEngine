package com.practice.searchengine.ui.initial

import androidx.lifecycle.ViewModelProvider
import com.practice.searchengine.App
import com.practice.searchengine.AppComponent
import dagger.Module
import dagger.Provides


@Module
class InitialFragmentModule {
    private val appComponent: AppComponent = App.instance.appComponent

    @Provides
    fun provideSplashViewModelFactory(): ViewModelProvider.Factory {
        return InitialViewModelFactory(InitialViewModel(appComponent.provideLogger(),
            appComponent.providePrefs()))
    }

    init {
        appComponent.injectInitialFragment(this)
    }
}