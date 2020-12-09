package com.siddhartho.trendingrepositories.viewmodel

import androidx.lifecycle.ViewModel
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoDataSource
import javax.inject.Inject

class DisplayReposViewModel @Inject constructor(val trendingRepoDataSource: TrendingRepoDataSource) :
    ViewModel() {

}