package com.practice.searchengine.model.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitService {
    @GET
    fun getPage(@Url url: String): Single<String>
}