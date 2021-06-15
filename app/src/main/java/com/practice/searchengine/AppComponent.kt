package com.practice.searchengine

import com.practice.searchengine.model.bfs_worker.BfsWorker
import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.model.prefs.Prefs
import com.practice.searchengine.model.network.NetworkClient
import com.practice.searchengine.ui.initial.InitialFragmentModule
import com.practice.searchengine.ui.search.SearchFragmentModule
import com.practice.searchengine.ui.splash.SplashFragmentModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun injectSplashFragment(module: SplashFragmentModule)
    fun injectSearchFragment(module: SearchFragmentModule)
    fun injectInitialFragment(module: InitialFragmentModule)
    fun provideLogger(): Log
    fun providePrefs(): Prefs
    fun provideNetworkClient(): NetworkClient
    fun provideBfsWorker(): BfsWorker
}