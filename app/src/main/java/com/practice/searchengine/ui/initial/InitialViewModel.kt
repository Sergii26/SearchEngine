package com.practice.searchengine.ui.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.searchengine.R
import com.practice.searchengine.model.logger.Log
import com.practice.searchengine.model.prefs.Prefs
import com.practice.searchengine.ui.arch.MvvmViewModel
import java.net.URL

class InitialViewModel(
    private val logger: Log,
    private val prefs: Prefs
) : MvvmViewModel(), InitialContract.ViewModel {

    private val valuesCheckedObservable = MutableLiveData<Boolean>()
    private val incorrectWebsiteObservable = MutableLiveData<Int>()
    private val incorrectThreadsCountObservable = MutableLiveData<Int>()
    private val incorrectSearchTextObservable = MutableLiveData<Int>()
    private val incorrectPagesLimitObservable = MutableLiveData<Int>()

    override fun getIncorrectSearchTextObservable(): LiveData<Int> {
        return incorrectSearchTextObservable
    }

    override fun getIncorrectThreadsCountObservable(): LiveData<Int> {
        return incorrectThreadsCountObservable
    }

    override fun getIncorrectWebsiteObservable(): LiveData<Int> {
        return incorrectWebsiteObservable
    }

    override fun getIncorrectPagesLimitObservable(): LiveData<Int> {
        return incorrectPagesLimitObservable
    }

    override fun getIsCheckedValues(): LiveData<Boolean> {
        return valuesCheckedObservable
    }

    override fun checkValues(website: String, searchText: String, threadsCount: String,
                             pagesLimit: String) {
        val websiteChecked = if(checkWebsite(website)) {
            true
        } else {
            incorrectWebsiteObservable.value = R.string.incorrect_website_error
            false
        }
        val searchTextChecked = if(checkSearchText(searchText)) {
            true
        } else {
            incorrectSearchTextObservable.value = R.string.incorrect_search_text_error
            false
        }
        val threadsCountChecked = if(checkNumber(threadsCount)) {
            true
        } else {
            incorrectThreadsCountObservable.value =R.string.incorrect_threads_count_error
            false
        }
        val pagesLimitChecked = if(checkNumber(pagesLimit)) {
            true
        } else {
            incorrectPagesLimitObservable.value = R.string.incorrect_pages_limit_error
            false
        }

        val isCorrectValues = websiteChecked && searchTextChecked && threadsCountChecked && pagesLimitChecked
        if(isCorrectValues) {
            prefs.putWebsite(website)
            prefs.putSearchText(searchText)
            prefs.putThreadsCount(threadsCount)
            prefs.putPagesLimit(pagesLimit)
            valuesCheckedObservable.value = true
        } else {
            valuesCheckedObservable.value = false
        }
    }

    private fun checkNumber(number: String): Boolean {
        return try {
            number.toInt() != 0
        } catch (e: Exception) {
            logger.log("InitialViewModel checkNumber() error: ${e.message}")
            false
        }
    }

    private fun checkWebsite(webSite: String): Boolean {
        return try {
            URL(webSite).toURI()
            true
        } catch (e: Exception) {
            logger.log("InitialViewModel checkWebsite() error: ${e.message}")
            false
        }
    }

    private fun checkSearchText(searchText: String): Boolean {
        return searchText.isNotEmpty() && searchText != "null"
    }

}