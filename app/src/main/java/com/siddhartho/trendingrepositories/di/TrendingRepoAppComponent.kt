package com.siddhartho.trendingrepositories.di

import android.app.Application
import com.siddhartho.trendingrepositories.TrendingRepoApplication
import com.siddhartho.trendingrepositories.di.viewmodel.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        TrendingRepoAppModule::class,
        ViewModelFactoryModule::class]
)
interface TrendingRepoAppComponent : AndroidInjector<TrendingRepoApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TrendingRepoAppComponent
    }
}