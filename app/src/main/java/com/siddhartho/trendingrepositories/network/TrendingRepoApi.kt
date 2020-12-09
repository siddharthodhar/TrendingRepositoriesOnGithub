package com.siddhartho.trendingrepositories.network

import com.siddhartho.trendingrepositories.model.RepoDetail
import io.reactivex.Flowable
import retrofit2.http.GET


interface TrendingRepoApi {

    @GET("repositories")
    fun getReposFromApi(): Flowable<List<RepoDetail>>
}