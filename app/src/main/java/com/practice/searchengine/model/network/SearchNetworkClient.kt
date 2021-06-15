package com.practice.searchengine.model.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class SearchNetworkClient : NetworkClient {
    private val baseUrl = "https://google.com"
    private val retrofitService: RetrofitService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RetrofitService::class.java)

    override fun getPage(url: String): Single<String> {
        return retrofitService.getPage(url)
    }
}