package com.practice.searchengine

import com.practice.searchengine.model.bfs_worker.BfsWorker
import com.practice.searchengine.model.bfs_worker.BfsWorkerImpl
import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.model.logger.Logger
import com.practice.searchengine.model.network.NetworkClient
import com.practice.searchengine.model.network.SearchNetworkClient
import com.practice.searchengine.model.prefs.Prefs
import com.practice.searchengine.model.prefs.PrefsImpl
import com.practice.searchengine.ui.initial.InitialViewModel
import com.practice.searchengine.ui.search.SearchViewModel
import com.practice.searchengine.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
    factory<Log> { Logger.withTag("AppLog") }

    single<NetworkClient> { SearchNetworkClient() }

    single<Prefs> { PrefsImpl(androidContext()) }

    factory<BfsWorker> { (prefs: Prefs) ->
        BfsWorkerImpl(
            get(), get(), prefs.getThreadsCount().toInt(), prefs.getPagesLimit().toInt(),
            prefs.getSearchText()
        )
    }

    viewModel { SplashViewModel(get()) }

    viewModel { InitialViewModel(get(), get()) }

    viewModel { SearchViewModel(get(), get { parametersOf(get<Prefs>())}, get()) }
}

