package com.siddhartho.trendingrepositories.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.siddhartho.trendingrepositories.model.RepoDetail
import com.siddhartho.trendingrepositories.network.RepoResource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Suppress("CAST_NEVER_SUCCEEDS")
class DisplayReposViewModel @Inject constructor(private val trendingRepoDataSource: TrendingRepoDataSource) :
    ViewModel() {

    private val disposables = CompositeDisposable()
    private var repoDetailListApi: MediatorLiveData<RepoResource<out List<RepoDetail>?>>? = null
    private var repoDetailListLocal: MediatorLiveData<List<RepoDetail>?>? = null

    fun observeTrendingReposFromApi(): LiveData<RepoResource<out List<RepoDetail>?>>? {
        Log.d(TAG, "observeTrendingReposFromApi() called")

        repoDetailListApi = MediatorLiveData()
        repoDetailListApi?.value = RepoResource.loading(null as List<RepoDetail>?)

        val source =
            LiveDataReactiveStreams.fromPublisher(
                trendingRepoDataSource.getReposFromApi()
                    .onErrorReturn { e ->
                        Log.e(TAG, "observeTrendingReposFromApi onErrorReturn: ${e.message}", e)
                        val repoDetail = RepoDetail(
                            "",
                            "",
                            "",
                            "",
                            e.message!!,
                            "",
                            "",
                            ""
                        )
                        repoDetail.repoId = -1
                        val tempRepoDetailList = ArrayList<RepoDetail>()
                        tempRepoDetailList.add(repoDetail)
                        return@onErrorReturn tempRepoDetailList
                    }
                    .map { listRepoDetails ->
                        Log.d(
                            TAG,
                            "observeTrendingReposFromApi() getListFromApi " +
                                    "map called with: listRepoDetails = $listRepoDetails"
                        )
                        if (listRepoDetails.isNotEmpty()) {
                            if (listRepoDetails[0].repoId == -1) {
                                return@map RepoResource.error(
                                    listRepoDetails[0].description,
                                    null
                                )
                            }
                        }
                        return@map RepoResource.success(listRepoDetails)
                    }
                    .subscribeOn(Schedulers.io())
            )

        repoDetailListApi?.addSource(source) { repoDetailListResource ->
            repoDetailListApi?.value = repoDetailListResource
            repoDetailListApi?.removeSource(source)
        }
        return repoDetailListApi
    }

    fun observeTrendingReposFromLocal(): LiveData<List<RepoDetail>?>? {
        Log.d(TAG, "observeTrendingReposFromLocal() called")

        repoDetailListLocal ?: run {
            repoDetailListLocal = MediatorLiveData()

            val source =
                LiveDataReactiveStreams.fromPublisher(
                    trendingRepoDataSource.getRepoDetails()
                        .onErrorReturn { e ->
                            Log.e(
                                TAG,
                                "observeTrendingReposFromLocal onErrorReturn: ${e.message}",
                                e
                            )
                            val repoDetail = RepoDetail(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                            repoDetail.repoId = -1
                            val tempRepoDetailList = ArrayList<RepoDetail>()
                            tempRepoDetailList.add(repoDetail)
                            return@onErrorReturn tempRepoDetailList
                        }
                        .subscribeOn(Schedulers.io())
                )

            repoDetailListLocal?.addSource(source) { repoDetailListResource ->
                repoDetailListLocal?.value = repoDetailListResource
            }
        }
        return repoDetailListLocal
    }

    fun insertRepoDetailsList(repoDetails: List<RepoDetail>) {
        Log.d(TAG, "insertRepoDetailsList() called")

        clearAllRepos()
            .subscribeOn(Schedulers.io())
            .andThen(trendingRepoDataSource.insertRepoDetails(repoDetails))
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "insertRepoDetailsList: insertion complete")
            }, { e ->
                Log.e(TAG, "insertRepoDetailsList: ${e.message}", e)
            }).let { disposable -> disposables.add(disposable) }
    }

    fun hasRepoDetailsInLocal(): Single<Boolean> {
        Log.d(TAG, "hasRepoDetailsInLocal() called")
        return trendingRepoDataSource.hasRepoDetails()
    }

    fun clearAllRepos(): Completable {
        Log.d(TAG, "clearAllRepos() called")
        return Completable.create { emitter ->
            hasRepoDetailsInLocal()
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { hasRepos ->
                    if (hasRepos)
                        return@flatMapCompletable trendingRepoDataSource.clearAllRepos()
                            .subscribeOn(Schedulers.io())
                    return@flatMapCompletable Completable.complete()
                }.subscribe({
                    emitter.onComplete()
                }, { e ->
                    emitter.onError(e)
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    companion object {
        private const val TAG = "DisplayReposViewModel"
    }

}