package com.practice.searchengine.model.prefs

import android.content.Context

class PrefsImpl(ctx: Context?) : BasicPrefsImpl(ctx!!), Prefs {
    override val defaultPrefsFileName: String
        get() = "socialNetwork"

    companion object {
        private const val KEY_WEBSITE = "website"
        private const val KEY_THREADS_COUNT = "threads_count"
        private const val KEY_SEARCH_TEXT = "search_text"
        private const val KEY_PAGES_LIMIT = "pages_limit"

    }

    override fun putWebsite(website: String) {
        put(KEY_WEBSITE, website)
    }

    override fun getWebsite(): String {
        return get(KEY_WEBSITE, "")
    }

    override fun putThreadsCount(threadsCount: String) {
        put(KEY_THREADS_COUNT, threadsCount)
    }

    override fun getThreadsCount(): String {
        return get(KEY_THREADS_COUNT, "")
    }

    override fun putSearchText(searchText: String) {
        put(KEY_SEARCH_TEXT, searchText)
    }

    override fun getSearchText(): String {
        return get(KEY_SEARCH_TEXT, "")
    }

    override fun putPagesLimit(pagesLimit: String) {
        put(KEY_PAGES_LIMIT, pagesLimit)
    }

    override fun getPagesLimit(): String {
        return get(KEY_PAGES_LIMIT, "")
    }

}
