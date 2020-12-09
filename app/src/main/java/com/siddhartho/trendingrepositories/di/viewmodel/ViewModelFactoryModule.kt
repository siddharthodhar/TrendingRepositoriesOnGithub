package com.siddhartho.trendingrepositories.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(trendingRepoViewModelFactory: TrendingRepoViewModelFactory): ViewModelProvider.Factory
}