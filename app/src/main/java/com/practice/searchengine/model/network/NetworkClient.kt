package com.practice.searchengine.model.network

import io.reactivex.Single

interface NetworkClient {
    fun getPage(url: String): Single<String>
}