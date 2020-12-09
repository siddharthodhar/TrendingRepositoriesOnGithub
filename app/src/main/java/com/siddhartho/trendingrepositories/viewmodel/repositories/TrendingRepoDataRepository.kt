package com.siddhartho.trendingrepositories.viewmodel.repositories

import com.siddhartho.trendingrepositories.local.TrendingRepoDao
import com.siddhartho.trendingrepositories.model.RepoDetail
import com.siddhartho.trendingrepositories.network.TrendingRepoApi
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoDataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class TrendingRepoDataRepository() : TrendingRepoDataSource {

    @Inject
    lateinit var trendingRepoDao: TrendingRepoDao

    @Inject
    lateinit var trendingRepoApi: TrendingRepoApi

    override fun getReposFromApi(): Flowable<List<RepoDetail>> {
        return trendingRepoApi.getReposFromApi()
    }

    override fun insertRepoDetails(repoDetails: List<RepoDetail>): Completable {
        return trendingRepoDao.insertRepoDetails(repoDetails)
    }

    override fun clearAllRepos(): Completable {
        return trendingRepoDao.clearAllRepos()
    }

    override fun getRepoDetails(): Flowable<List<RepoDetail>> {
        return trendingRepoDao.getRepoDetails()
    }
}