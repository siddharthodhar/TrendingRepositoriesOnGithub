package com.siddhartho.trendingrepositories.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddhartho.trendingrepositories.R
import com.siddhartho.trendingrepositories.databinding.ActivityDisplayReposBinding
import com.siddhartho.trendingrepositories.network.RepoResource
import com.siddhartho.trendingrepositories.utils.showToast
import com.siddhartho.trendingrepositories.viewmodel.DisplayReposViewModel
import com.siddhartho.trendingrepositories.viewmodel.TrendingRepoViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DisplayReposActivity : DaggerAppCompatActivity() {

    private val disposables = CompositeDisposable()
    private var displayReposViewModel: DisplayReposViewModel? = null
    private lateinit var binding: ActivityDisplayReposBinding

    @Inject
    lateinit var trendingRepoViewModelFactory: TrendingRepoViewModelFactory

    @Inject
    lateinit var displayReposRecyclerViewAdapter: DisplayReposRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayReposBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()

        displayReposViewModel = ViewModelProvider(this, trendingRepoViewModelFactory)
            .get(DisplayReposViewModel::class.java)

        subscribeLocalObserver()

        subscribeApiObserver()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState() called with: outState = $outState")
        if (binding.searchViewRepos.query.isNotEmpty()) {
            outState.putString(SEARCH_QUERY, binding.searchViewRepos.query.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState() called with: savedInstanceState = $savedInstanceState")
        if (savedInstanceState.containsKey(SEARCH_QUERY)) {
            Completable.timer(1, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    binding.searchViewRepos.setQuery(
                        savedInstanceState.getString(SEARCH_QUERY),
                        true
                    )
                }.let { disposable -> disposables.add(disposable) }
        }
    }

    private fun setUpToolbar() {
        Log.d(TAG, "setUpToolbar() called")
        setSupportActionBar(binding.toolbarDisplayRepos)

        binding.searchViewRepos.apply {

            setOnSearchClickListener {
                binding.toolbarDisplayRepos.setNavigationIcon(R.drawable.ic_back_arrow)
            }

            setOnCloseListener {
                binding.toolbarDisplayRepos.navigationIcon = null
                if (!isIconified) {
                    onActionViewCollapsed()
                }

                return@setOnCloseListener true
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "onQueryTextSubmit() called with: query = $query")
                    displayReposRecyclerViewAdapter.filter.filter(query)
                    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(binding.searchViewRepos.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "onQueryTextChange() called with: newText = $newText")
                    if (!isIconified) {
                        binding.toolbarDisplayRepos.setNavigationIcon(R.drawable.ic_back_arrow)
                    }
                    displayReposRecyclerViewAdapter.filter.filter(newText)
                    return true
                }
            })
        }

        binding.toolbarDisplayRepos.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initDisplayRepoRecyclerView() {
        Log.d(TAG, "initDisplayRepoRecyclerView() called")
        binding.recyclerViewDisplayRepos.apply {
            layoutManager =
                LinearLayoutManager(this@DisplayReposActivity, LinearLayoutManager.VERTICAL, false)
            adapter = displayReposRecyclerViewAdapter
        }
    }

    private fun subscribeLocalObserver() {
        displayReposViewModel?.observeTrendingReposFromLocal()?.apply {

            removeObservers(this@DisplayReposActivity)
            observe(this@DisplayReposActivity, { repoDetailsList ->
                repoDetailsList?.let { listRepo ->
                    Log.d(TAG, "subscribeLocalObserver() called with: listRepo = $listRepo")
                    if (listRepo.isNotEmpty()) {
                        showData()
                        displayReposRecyclerViewAdapter.setRepoDetailList(listRepo)
                    }
                }
            })
        }
    }

    private fun subscribeApiObserver() {
        var disposableLoading: Disposable? = null
        displayReposViewModel?.observeTrendingReposFromApi()?.apply {

            removeObservers(this@DisplayReposActivity)
            observe(this@DisplayReposActivity, { repoDetailListResource ->
                repoDetailListResource?.let { listRepoResource ->
                    when (listRepoResource.status) {
                        RepoResource.Status.SUCCESS -> {
                            Log.d(
                                TAG,
                                "subscribeApiObserver: success with data ${listRepoResource.data}"
                            )

                            listRepoResource.data?.let { listRepo ->
                                if (listRepo.isNotEmpty()) {
                                    displayReposViewModel?.insertRepoDetailsList(listRepo)
                                }
                            }

                            if (binding.swipeRefreshRepos.isVisible && binding.swipeRefreshRepos.isRefreshing) {
                                binding.swipeRefreshRepos.isRefreshing = false
                            }
                        }
                        RepoResource.Status.LOADING -> {
                            Log.d(TAG, "subscribeApiObserver: loading")

                            displayReposViewModel?.hasRepoDetailsInLocal()
                                ?.subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe({ hasRepos ->
                                    if (!hasRepos) {
                                        showLoader()
                                    }
                                }, { e ->
                                    Log.e(TAG, "subscribeApiObserver: ${e.message}", e)
                                    showError("Something went wrong! (${e.message})")
                                })?.let { disposable ->
                                    disposableLoading = disposable
                                    disposables.add(disposable)
                                }
                        }
                        RepoResource.Status.ERROR -> {
                            Log.d(
                                TAG,
                                "subscribeApiObserver: error occurred ${listRepoResource.message}"
                            )

                            disposableLoading?.dispose()
                            displayReposViewModel?.hasRepoDetailsInLocal()
                                ?.subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe({ hasRepos ->
                                    if (!hasRepos) {
                                        showError("Something went wrong! (${listRepoResource.message})")
                                    } else {
                                        showToast(R.string.something_wrong)
                                    }
                                }, { e ->
                                    Log.e(TAG, "subscribeApiObserver: ${e.message}", e)
                                    showError("Something went wrong! (${e.message})")
                                })?.let { disposable -> disposables.add(disposable) }

                            if (binding.swipeRefreshRepos.isVisible && binding.swipeRefreshRepos.isRefreshing) {
                                binding.swipeRefreshRepos.isRefreshing = false
                            }
                        }
                    }
                }
            })
        }
    }

    private fun showLoader() {
        Log.d(TAG, "showLoader() called")

        binding.apply {
            if (!progressBarLoadRepos.isVisible) {
                progressBarLoadRepos.visibility = View.VISIBLE
            }
            if (swipeRefreshRepos.isVisible) {
                swipeRefreshRepos.visibility = View.GONE
            }
            if (linearLayoutErrorTryAgain.isVisible) {
                if (buttonTryAgainLoadRepos.hasOnClickListeners()) {
                    buttonTryAgainLoadRepos.setOnClickListener(null)
                }
                linearLayoutErrorTryAgain.visibility = View.GONE
            }
        }
    }

    private fun showData() {
        Log.d(TAG, "showData() called")

        binding.apply {
            if (!swipeRefreshRepos.isVisible) {
                swipeRefreshRepos.visibility = View.VISIBLE
                initDisplayRepoRecyclerView()
                swipeRefreshRepos.setOnRefreshListener {
                    subscribeApiObserver()
                }
            }
            if (progressBarLoadRepos.isVisible) {
                progressBarLoadRepos.visibility = View.GONE
            }
            if (linearLayoutErrorTryAgain.isVisible) {
                if (buttonTryAgainLoadRepos.hasOnClickListeners()) {
                    buttonTryAgainLoadRepos.setOnClickListener(null)
                }
                linearLayoutErrorTryAgain.visibility = View.GONE
            }
        }
    }

    private fun showError(message: String) {
        Log.d(TAG, "showError() called with: message = $message")

        binding.apply {
            if (!linearLayoutErrorTryAgain.isVisible) {
                linearLayoutErrorTryAgain.visibility = View.VISIBLE
                textViewErrorMessage.text = message
                buttonTryAgainLoadRepos.setOnClickListener {
                    subscribeApiObserver()
                }
            }
            if (progressBarLoadRepos.isVisible) {
                progressBarLoadRepos.visibility = View.GONE
            }
            if (swipeRefreshRepos.isVisible) {
                swipeRefreshRepos.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed() called")

        binding.apply {
            toolbarDisplayRepos.navigationIcon?.let {
                if (!searchViewRepos.isIconified) {
                    toolbarDisplayRepos.navigationIcon = null
                    searchViewRepos.onActionViewCollapsed()
                    return
                }
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

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    companion object {
        private const val TAG = "DisplayReposActivity"
        private var isBackPressed = false
        private const val SEARCH_QUERY = "search_query"
    }

}