package com.siddhartho.trendingrepositories.viewmodel.repositories

import com.siddhartho.trendingrepositories.local.TrendingRepoDao
import com.siddhartho.trendingrepositories.model.RepoDetail
import com.siddhartho.trendingrepositories.network.TrendingRepoApi
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoDataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class TrendingRepoDataRepository(
    private val trendingRepoDao: TrendingRepoDao,
    private val trendingRepoApi: TrendingRepoApi
) : TrendingRepoDataSource {

    override fun getReposFromApi(): Flowable<List<RepoDetail>> {
        return trendingRepoApi.getReposFromApi()
    }

    override fun insertRepoDetails(repoDetails: List<RepoDetail>): Completable {
        return trendingRepoDao.insertRepoDetails(repoDetails)
    }

    override fun hasRepoDetails(): Single<Boolean> {
        return trendingRepoDao.hasRepoDetails()
    }

    override fun clearAllRepos(): Completable {
        return trendingRepoDao.clearAllRepos()
    }

    override fun getRepoDetails(): Flowable<List<RepoDetail>> {
        return trendingRepoDao.getRepoDetails()
    }
}