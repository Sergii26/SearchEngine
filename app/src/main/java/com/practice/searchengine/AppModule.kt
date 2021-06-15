package com.practice.searchengine

import com.practice.searchengine.model.bfs_worker.BfsWorker
import com.practice.searchengine.model.bfs_worker.BfsWorkerImpl
import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.model.logger.Logger
import com.practice.searchengine.model.prefs.Prefs
import com.practice.searchengine.model.prefs.PrefsImpl
import com.practice.searchengine.model.network.NetworkClient
import com.practice.searchengine.model.network.SearchNetworkClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    fun provideLogger(): Log {
        return Logger.withTag("AppLog")
    }

    @Provides
    @Singleton
    fun provideNetworkClient(): NetworkClient {
        return SearchNetworkClient()
    }

    @Provides
    @Singleton
    fun providePrefs(): Prefs {
        return PrefsImpl(App.instance)
    }

    @Provides
    fun provideBfsWorker(): BfsWorker{
        val appComponent = App.instance.appComponent
        val prefs = appComponent.providePrefs()
        return BfsWorkerImpl(appComponent.provideLogger(), appComponent.provideNetworkClient(),
            prefs.getThreadsCount().toInt(), prefs.getPagesLimit().toInt(), prefs.getSearchText() )
    }
}