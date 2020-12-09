package com.siddhartho.trendingrepositories.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.siddhartho.trendingrepositories.model.RepoDetail

@Database(entities = [RepoDetail::class], version = TrendingRepoDatabase.VERSION)
abstract class TrendingRepoDatabase : RoomDatabase() {

    abstract fun trendingRepoDao(): TrendingRepoDao

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "trending_repos_database.db"
    }
}