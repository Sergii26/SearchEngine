package com.practice.searchengine.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.searchengine.model.bfs_worker.BfsWorker
import com.practice.searchengine.model.bfs_worker.PageResult
import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.model.prefs.Prefs
import com.practice.searchengine.ui.arch.MvvmViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel(
    private val logger: Log,
    private val bfs: BfsWorker,
    private val prefs: Prefs
) : MvvmViewModel(), SearchContract.ViewModel {

    private val searchedCountObservable = MutableLiveData<Int>()
    private val searchingCountObservable = MutableLiveData<Int>()
    private val progressObservable = MutableLiveData<Int>().apply { postValue(0) }
    private val resultObservable = MutableLiveData<PageResult>()
    private val searchingStateObservable =
        MutableLiveData<String>().apply { postValue(SearchingStateIndication.STATE_SEARCH) }
    private val isCompleteObservable = MutableLiveData<Boolean>().apply { postValue(false) }
    private val isErrorObservable = MutableLiveData<Boolean>().apply { postValue(false) }

    override fun getIsCompleteObservable(): LiveData<Boolean> {
        return isCompleteObservable
    }

    override fun getIsErrorObservable(): LiveData<Boolean> {
        return isErrorObservable
    }

    override fun getSearchingStateObservable(): LiveData<String> {
        return searchingStateObservable
    }

    override fun getSearchedCountObservable(): LiveData<Int> {
        return searchedCountObservable
    }

    override fun getSearchingCountObservable(): LiveData<Int> {
        return searchingCountObservable
    }

    override fun getProgressObservable(): LiveData<Int> {
        return progressObservable
    }

    override fun getResultsObservable(): LiveData<PageResult> {
        return resultObservable
    }

    override fun startSearch() {
        bfs.fetchPagesViaBfs(prefs.getWebsite())
        subscribeToBfsData()
        searchingStateObservable.value = SearchingStateIndication.STATE_SEARCH
    }

    override fun pauseSearch() {
        bfs.pauseSearch()
        searchingStateObservable.value = SearchingStateIndication.STATE_PAUSE
    }

    override fun resumeSearch() {
        bfs.resumeSearch()
        searchingStateObservable.value = SearchingStateIndication.STATE_SEARCH
    }

    override fun stopSearch() {
        bfs.pauseSearch()
        progressObservable.value = 100
        searchingStateObservable.value = SearchingStateIndication.STATE_STOP
    }

    private fun subscribeToBfsData() {
        val pagesLimit = prefs.getPagesLimit().toInt()
        val progressStep: Double = (pagesLimit.toDouble() / 100.0)
        compositeDisposable.add(
            bfs.getSearchedPagesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressObservable.value = (it / progressStep).toInt()
                },
                    {
                        logger.log("SearchViewModel subscribeToBfsData() searched branch error: ${it.message}")
                    })
        )

        compositeDisposable.add(
            bfs.getSearchingPagesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it > 0) {
                        searchingCountObservable.value = it
                        searchedCountObservable.value = pagesLimit - it
                    } else {
                        searchingCountObservable.value = 0
                        searchedCountObservable.value = pagesLimit
                    }
                },
                    {
                        logger.log("SearchViewModel subscribeToBfsData() searching branch error: ${it.message}")
                    })
        )

        compositeDisposable.add(
            bfs.getSearchResultObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    resultObservable.value = it
                }, {
                    logger.log("SearchViewModel subscribeToBfsData() result branch error: ${it.message}")
                })
        )

        compositeDisposable.add(
            bfs.getIsCompleteObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it && isCompleteObservable.value == false) {
                        isCompleteObservable.value = true
                        searchingStateObservable.value = SearchingStateIndication.STATE_STOP
                        searchingCountObservable.value = 0
                        searchedCountObservable.value = pagesLimit
                        progressObservable.value = 100
                    }
                }, {
                    logger.log("SearchViewModel subscribeToBfsData() complete branch error: ${it.message}")
                })
        )

        compositeDisposable.add(
            bfs.getIsErrorObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        isErrorObservable.value = true
                        searchingStateObservable.value = SearchingStateIndication.STATE_STOP
                    }
                }, {
                    logger.log("SearchViewModel subscribeToBfsData() error branch error: ${it.message}")
                })
        )
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        bfs.clearDisposables()
    }
}