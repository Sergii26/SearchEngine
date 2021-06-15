package com.practice.searchengine.ui.initial

import androidx.lifecycle.LiveData
import com.practice.searchengine.ui.arch.FragmentContract

interface InitialContract {

    interface ViewModel : FragmentContract.ViewModel{
        fun checkValues(website: String, searchText: String, threadsCount: String, pagesLimit: String)
        fun getIncorrectWebsiteObservable(): LiveData<Int>
        fun getIncorrectThreadsCountObservable(): LiveData<Int>
        fun getIncorrectSearchTextObservable(): LiveData<Int>
        fun getIncorrectPagesLimitObservable(): LiveData<Int>
        fun getIsCheckedValues(): LiveData<Boolean>
    }

    interface Host : FragmentContract.Host {
        fun openSearchFragment()
    }
}