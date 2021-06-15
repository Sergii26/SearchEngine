package com.practice.searchengine.model.bfs_worker

import io.reactivex.Observable

interface BfsWorker {
    fun fetchPagesViaBfs(initialPage: String)
    fun getSearchedPagesObservable(): Observable<Int>
    fun getSearchingPagesObservable(): Observable<Int>
    fun getSearchResultObservable(): Observable<PageResult>
    fun getIsCompleteObservable(): Observable<Boolean>
    fun getIsErrorObservable(): Observable<Boolean>
    fun pauseSearch()
    fun resumeSearch()
    fun clearDisposables()
}