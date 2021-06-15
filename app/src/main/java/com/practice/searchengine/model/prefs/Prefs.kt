package com.practice.searchengine.model.prefs

interface Prefs {
    fun putWebsite(website: String)
    fun getWebsite(): String
    fun putThreadsCount(threadsCount: String)
    fun getThreadsCount(): String
    fun putSearchText(searchText: String)
    fun getSearchText(): String
    fun putPagesLimit(pagesLimit: String)
    fun getPagesLimit(): String
}
