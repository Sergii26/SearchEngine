package com.practice.searchengine.model.bfs_worker

import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.model.network.NetworkClient
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.*
import kotlin.collections.ArrayList

class BfsWorkerImpl(
    private val logger: Log,
    private val apiService: NetworkClient,
    private val threadsCount: Int,
    private val pagesLimit: Int,
    private val searchString: String
) : BfsWorker {
    //unique pages which was searched
    private var visitedPages = ConcurrentHashMap.newKeySet<String>(pagesLimit)
    //pages for searching
    private var pagesQueue = ConcurrentLinkedQueue<String>()
    //list for fetching urls in multithreading. Every one thread has own index
    private val jobUrlsList = CopyOnWriteArrayList<String>()
    private val searchTimeStart = System.currentTimeMillis()
    private var searchTimeEnd: Long = 0
    private var isPaused: Boolean = false

    //urls count which waits for searching
    private val searchingUrlsCountObservable = BehaviorSubject.createDefault(0)
    //urls count which have already searched
    private val searchedUrlsCountObservable = BehaviorSubject.createDefault(0)
    //result with unique url, coincidence count or error
    private val searchResultObservable = BehaviorSubject.create<PageResult>()

    private val compositeDisposable = CompositeDisposable()

    private val emptyString = ""
    private val isCompleteObservable = BehaviorSubject.createDefault(false)
    private val isErrorObservable = BehaviorSubject.createDefault(false)

    companion object {
        const val URL_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!\$&'()*+,;= "
    }

    override fun getIsCompleteObservable(): Observable<Boolean> {
        return isCompleteObservable
    }

    override fun getIsErrorObservable(): Observable<Boolean> {
        return isErrorObservable
    }

    override fun getSearchedPagesObservable(): Observable<Int> {
        return searchedUrlsCountObservable
    }

    override fun getSearchingPagesObservable(): Observable<Int> {
        return searchingUrlsCountObservable
    }

    override fun getSearchResultObservable(): Observable<PageResult> {
        return searchResultObservable
    }

    override fun pauseSearch() {
        isPaused = true
        if (compositeDisposable.size() != 0) {
            compositeDisposable.clear()
        }
    }

    override fun resumeSearch() {
        isPaused = false
        val threadPool = Schedulers.from(Executors.newFixedThreadPool(threadsCount))
        for (i in 0 until threadsCount) {
            oneThreadJob(threadPool, i)
        }
    }

    // enter point
    override fun fetchPagesViaBfs(initialPage: String) {
        compositeDisposable.add(
            apiService.getPage(initialPage)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response ->
                        val pageUrls = getPageUrls(response)
                        pagesQueue.addAll(pageUrls)
                        visitedPages.add(initialPage)
                        searchedUrlsCountObservable.onNext(searchedUrlsCountObservable.value?.plus(1)!!)
                        searchingUrlsCountObservable.onNext(pagesLimit - visitedPages.size)
                        searchResultObservable.onNext(
                            PageResult(
                                initialPage,
                                getCoincidenceCount(response).toString(),
                                emptyString
                            )
                        )
                        findPagesByThreads()
                    },
                    { error ->
                        logger.log("BfsWorkerImpl fetchPagesViaBfs error: ${error.message}")
                        isErrorObservable.onNext(true)
                    })
        )
    }

    private fun findPagesByThreads() {
        val threadPool = Schedulers.from(Executors.newFixedThreadPool(threadsCount))
        setFirstValuesToJobQueue()
        for (i in 0 until threadsCount) {
            oneThreadJob(threadPool, i)
        }
    }

    private fun setFirstValuesToJobQueue() {
        // TODO: error when urls in initial page less then threads count
        for (i in 0 until threadsCount) {
            jobUrlsList.add(i, pagesQueue.remove())
            if (pagesQueue.isEmpty()) {
                return
            }
        }
    }

    private fun oneThreadJob(threadPoll: Scheduler, index: Int) {
        val threadSubject = BehaviorSubject.createDefault(jobUrlsList[index])
        compositeDisposable.add(
            threadSubject
                .subscribeOn(threadPoll)
                .takeWhile {
                    visitedPages.size < pagesLimit
                }
                .flatMap {
                    return@flatMap apiService.getPage(it).toObservable()
                        .onErrorResumeNext { error: Throwable ->
                            if(visitedPages.add(jobUrlsList[index])){
                                updateSearchingSubjects(
                                    visitedPages.size,
                                    index,
                                    0,
                                    error.message.toString()
                                )
                            }
                            Single.just("").toObservable()
                        }
                }
                .subscribe(
                    { result ->
                        if (result != "") {
                            val urls = getPageUrls(result)
                            urls.forEach {
                                if (!visitedPages.contains(it) && it != "https://") {
                                    pagesQueue.add(it)
                                }
                            }
                            if(visitedPages.add(jobUrlsList[index])) {
                                updateSearchingSubjects(
                                    visitedPages.size,
                                    index,
                                    getCoincidenceCount(result),
                                    emptyString
                                )
                            }
                        }
                        jobUrlsList[index] = pagesQueue.remove()
                        if (!isPaused) {
                            threadSubject.onNext(jobUrlsList[index])
                        }

                    }, { error ->
                        isErrorObservable.onNext(true)
                        logger.log("SearchViewModel findPagesViaBfs error: " + error.message)
                    },
                    {
                        if (isCompleteObservable.value == false) {
                            isCompleteObservable.onNext(true)
                        }
                        searchTimeEnd = System.currentTimeMillis()
                        logger.log("SearchViewModel findPagesViaBfs onComplete, calculation time: ${(searchTimeEnd - searchTimeStart) / 1000}")
                        logger.log("SearchViewModel findPagesViaBfs onComplete, calculation visited pages size: ${visitedPages.size}")
                    }
                )
        )

    }

    private fun updateSearchingSubjects(
        visitedPagesCount: Int,
        index: Int,
        coincidenceCount: Int,
        error: String
    ) {
        searchedUrlsCountObservable.onNext(visitedPagesCount)
        searchingUrlsCountObservable.onNext(pagesLimit - visitedPagesCount)
        searchResultObservable.onNext(
            PageResult(
                jobUrlsList[index],
                coincidenceCount.toString(),
                error
            )
        )
    }

    private fun getPageUrls(page: String): List<String> {
        val splitPage = page.split("https://")
        val urls = ArrayList<String>(splitPage.size)
        var endIndex: Int
        var substring: String
        splitPage.forEach {
            endIndex = findIndexOfFirstUnUrlChar(it)
            substring = it.substring(0, endIndex)
            urls.add("https://$substring")
        }
        return urls
    }

    private fun findIndexOfFirstUnUrlChar(page: String): Int {
        var firstNoUrlCharIndex: Int = -1
        var char: Char
        for (i in 0 until page.length - 1) {
            char = page[i]
            if (URL_CHARS.indexOf(char) == -1 && firstNoUrlCharIndex == -1) {
                firstNoUrlCharIndex = i
                break
            }
        }
        if (firstNoUrlCharIndex == -1) {
            //there is not url char -> return full string
            return page.length - 1
        } else {
            return firstNoUrlCharIndex
        }
    }

    private fun getCoincidenceCount(page: String): Int {
        return page.split(searchString).size - 1
    }

    override fun clearDisposables() {
        if (compositeDisposable.size() != 0) {
            compositeDisposable.clear()
        }
    }
}
