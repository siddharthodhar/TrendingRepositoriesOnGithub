package com.siddhartho.trendingrepositories.local

import androidx.room.*
import com.siddhartho.trendingrepositories.model.RepoDetail
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TrendingRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertRepoDetails(repoDetails: List<RepoDetail>): Completable

    @Query("SELECT EXISTS(SELECT repoId FROM repo_details)")
    fun hasRepoDetails(): Single<Boolean>

    @Query("DELETE FROM repo_details")
    fun clearAllRepos(): Completable

    @Query("SELECT * FROM repo_details")
    fun getRepoDetails(): Flowable<List<RepoDetail>>
}