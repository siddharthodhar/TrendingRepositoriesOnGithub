package com.siddhartho.trendingrepositories.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.siddhartho.trendingrepositories.R
import com.siddhartho.trendingrepositories.di.ui.ActivityScope
import com.siddhartho.trendingrepositories.local.TrendingRepoDatabase
import com.siddhartho.trendingrepositories.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class TrendingRepoAppModule {

    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideTrendingRepoDatabase(application: Application) =
            Room.databaseBuilder(
                application,
                TrendingRepoDatabase::class.java,
                TrendingRepoDatabase.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

        @JvmStatic
        @Singleton
        @Provides
        fun provideRetrofitInstance(): Retrofit =
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @JvmStatic
        @Singleton
        @Provides
        fun provideRequestOptions() =
            RequestOptions
                .placeholderOf(R.drawable.ic_language)
                .error(R.drawable.ic_language)

        @JvmStatic
        @Singleton
        @Provides
        fun provideGlideInstance(application: Application, requestOptions: RequestOptions) =
            Glide.with(application)
                .setDefaultRequestOptions(requestOptions)

    }
}