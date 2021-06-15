package com.practice.searchengine.ui.search

import androidx.lifecycle.ViewModelProvider
import com.practice.searchengine.App
import com.practice.searchengine.AppComponent
import dagger.Module
import dagger.Provides


@Module
class SearchFragmentModule {
    private val appComponent: AppComponent = App.instance.appComponent

    @Provides
    fun provideSplashViewModelFactory(): ViewModelProvider.Factory {
        return SearchViewModelFactory(SearchViewModel(appComponent.provideLogger(),
            appComponent.provideBfsWorker(), appComponent.providePrefs()))
    }

    init {
        appComponent.injectSearchFragment(this)
    }
}