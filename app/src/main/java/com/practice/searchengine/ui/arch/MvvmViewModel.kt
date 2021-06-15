package com.practice.searchengine.ui.arch

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class MvvmViewModel: ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}