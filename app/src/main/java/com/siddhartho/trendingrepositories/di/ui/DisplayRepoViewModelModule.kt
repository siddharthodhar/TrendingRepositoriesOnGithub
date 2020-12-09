package com.siddhartho.trendingrepositories.di.ui

import androidx.lifecycle.ViewModel
import com.siddhartho.trendingrepositories.di.viewmodel.ViewModelKey
import com.siddhartho.trendingrepositories.viewmodel.DisplayReposViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DisplayRepoViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DisplayReposViewModel::class)
    abstract fun bindDisplayReposViewModel(displayReposViewModel: DisplayReposViewModel): ViewModel
}