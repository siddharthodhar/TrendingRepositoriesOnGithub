package com.siddhartho.trendingrepositories

import com.siddhartho.trendingrepositories.di.DaggerTrendingRepoAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class TrendingRepoApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTrendingRepoAppComponent.builder().application(this).build()
    }
}