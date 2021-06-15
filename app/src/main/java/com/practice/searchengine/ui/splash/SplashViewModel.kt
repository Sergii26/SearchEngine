package com.practice.searchengine.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.ui.arch.MvvmViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(private val logger: Log) : MvvmViewModel(), SplashContract.ViewModel {
    private val isReady = MutableLiveData<Boolean>()

    override fun getIsReady(): LiveData<Boolean> {
        return isReady;
    }

    override fun startTimer() {
        isReady.value = false
        compositeDisposable.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isReady.value = true }
                ) { throwable: Throwable -> logger.log("SplashViewModel startTimer() error: " + throwable.message) })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}