package com.siddhartho.trendingrepositories.di.ui

import com.siddhartho.trendingrepositories.local.TrendingRepoDao
import com.siddhartho.trendingrepositories.local.TrendingRepoDatabase
import com.siddhartho.trendingrepositories.network.TrendingRepoApi
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoDataSource
import com.siddhartho.trendingrepositories.viewmodel.repositories.TrendingRepoDataRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class DisplayReposModule {

    @Module
    companion object {
        @JvmStatic
        @ActivityScope
        @Provides
        fun provideTrendingRepoDataSource(
            trendingRepoDao: TrendingRepoDao,
            trendingRepoApi: TrendingRepoApi
        ): TrendingRepoDataSource = TrendingRepoDataRepository(trendingRepoDao, trendingRepoApi)

        @JvmStatic
        @ActivityScope
        @Provides
        fun provideTrendingRepoDao(trendingRepoDatabase: TrendingRepoDatabase): TrendingRepoDao =
            trendingRepoDatabase.trendingRepoDao()

        @JvmStatic
        @ActivityScope
        @Provides
        fun provideTrendingRepoApi(retrofit: Retrofit): TrendingRepoApi =
            retrofit.create(TrendingRepoApi::class.java)
    }
}