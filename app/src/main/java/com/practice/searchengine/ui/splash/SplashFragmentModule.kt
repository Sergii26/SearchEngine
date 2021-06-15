package com.practice.searchengine.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.practice.searchengine.App
import com.practice.searchengine.AppComponent
import dagger.Module
import dagger.Provides


@Module
class SplashFragmentModule {
    private val appComponent: AppComponent = App.instance.appComponent

    @Provides
    fun provideSplashViewModelFactory(): ViewModelProvider.Factory {
        return SplashViewModelFactory(SplashViewModel(appComponent.provideLogger()))
    }

    init {
        appComponent.injectSplashFragment(this)
    }
}