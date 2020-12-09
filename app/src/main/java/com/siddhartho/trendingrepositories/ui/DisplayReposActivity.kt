package com.siddhartho.trendingrepositories.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.siddhartho.trendingrepositories.R
import com.siddhartho.trendingrepositories.databinding.ActivityDisplayReposBinding
import com.siddhartho.trendingrepositories.utils.showToast
import com.siddhartho.trendingrepositories.viewmodel.DisplayReposViewModel
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DisplayReposActivity : DaggerAppCompatActivity() {

    private val disposables = CompositeDisposable()
    private var displayReposViewModel: DisplayReposViewModel? = null
    private lateinit var binding: ActivityDisplayReposBinding

    @Inject
    lateinit var trendingRepoViewModelFactory: TrendingRepoViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayReposBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()

        displayReposViewModel = ViewModelProvider(this, trendingRepoViewModelFactory)
            .get(DisplayReposViewModel::class.java)
    }

    private fun setUpToolbar() {
        Log.d(TAG, "setUpToolbar() called")
        setSupportActionBar(binding.toolbarDisplayRepos)

        binding.searchViewRepos.setOnSearchClickListener {
            binding.toolbarDisplayRepos.setNavigationIcon(R.drawable.ic_back_arrow)
        }

        binding.searchViewRepos.setOnCloseListener {
            binding.toolbarDisplayRepos.navigationIcon = null
            if (binding.searchViewRepos.isShown) {
                binding.searchViewRepos.onActionViewCollapsed()
            }

            return@setOnCloseListener true
        }

        binding.toolbarDisplayRepos.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed() called")

        binding.toolbarDisplayRepos.navigationIcon?.let {
            if (binding.searchViewRepos.isShown) {
                binding.toolbarDisplayRepos.navigationIcon = null
                binding.searchViewRepos.onActionViewCollapsed()
                return
            }
        }

        if (isBackPressed) {
            super.onBackPressed()
            finishAffinity()
        } else {
            isBackPressed = true
            showToast(R.string.tap_back_again)
            Completable.timer(1500, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe { isBackPressed = false }
                .let { disposable -> disposables.add(disposable) }
        }
    }

    companion object {
        private const val TAG = "DisplayReposActivity"
        private var isBackPressed = false
    }

}