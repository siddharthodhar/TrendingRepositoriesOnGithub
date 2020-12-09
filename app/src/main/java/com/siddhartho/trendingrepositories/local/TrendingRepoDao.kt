package com.siddhartho.trendingrepositories.local

import androidx.room.*
import com.siddhartho.trendingrepositories.model.RepoDetail
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TrendingRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertRepoDetails(repoDetails: List<RepoDetail>): Completable

    @Query("DELETE FROM repo_details")
    fun clearAllRepos(): Completable

    @Query("SELECT * FROM repo_details")
    fun getRepoDetails(): Flowable<List<RepoDetail>>
}