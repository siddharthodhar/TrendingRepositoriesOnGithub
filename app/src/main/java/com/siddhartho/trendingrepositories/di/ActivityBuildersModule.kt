package com.siddhartho.trendingrepositories.di

import com.siddhartho.trendingrepositories.di.ui.ActivityScope
import com.siddhartho.trendingrepositories.di.ui.DisplayRepoViewModelModule
import com.siddhartho.trendingrepositories.di.ui.DisplayReposModule
import com.siddhartho.trendingrepositories.ui.DisplayReposActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [DisplayReposModule::class, DisplayRepoViewModelModule::class]
    )
    abstract fun contributeDisplayReposActivity(): DisplayReposActivity
}