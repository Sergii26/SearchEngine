package com.practice.searchengine.ui.search

import androidx.lifecycle.LiveData
import com.practice.searchengine.model.bfs_worker.PageResult
import com.practice.searchengine.ui.arch.FragmentContract

interface SearchContract {

    interface ViewModel : FragmentContract.ViewModel{
        fun startSearch()
        fun getSearchingStateObservable(): LiveData<String>
        fun getSearchedCountObservable(): LiveData<Int>
        fun getSearchingCountObservable(): LiveData<Int>
        fun getProgressObservable(): LiveData<Int>
        fun getResultsObservable(): LiveData<PageResult>
        fun getIsCompleteObservable(): LiveData<Boolean>
        fun getIsErrorObservable(): LiveData<Boolean>
        fun pauseSearch()
        fun resumeSearch()
        fun stopSearch()

    }

    interface Host : FragmentContract.Host {
        fun backToInitialFragment()
    }
}