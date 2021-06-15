package com.practice.searchengine.ui.splash

import androidx.lifecycle.LiveData
import com.practice.searchengine.ui.arch.FragmentContract


interface SplashContract {
    interface ViewModel : FragmentContract.ViewModel {
        fun startTimer()
        fun getIsReady(): LiveData<Boolean>
    }

    interface Host : FragmentContract.Host {
        fun openInitialFragment()
    }
}