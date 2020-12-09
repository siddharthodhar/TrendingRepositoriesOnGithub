package com.siddhartho.trendingrepositories.viewmodel

import com.siddhartho.trendingrepositories.model.RepoDetail
import io.reactivex.Completable
import io.reactivex.Flowable

interface TrendingRepoDataSource {

    fun getReposFromApi(): Flowable<List<RepoDetail>>

    fun insertRepoDetails(repoDetails: List<RepoDetail>): Completable

    fun clearAllRepos(): Completable

    fun getRepoDetails(): Flowable<List<RepoDetail>>
}