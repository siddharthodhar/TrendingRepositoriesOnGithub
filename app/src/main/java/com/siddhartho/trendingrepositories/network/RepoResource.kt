package com.siddhartho.trendingrepositories.network

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class RepoResource<T>(
    @NonNull
    val status: Status,
    @Nullable
    val data: T,
    @Nullable
    val message: String?
) {

    companion object {
        fun <T> success(data: T): RepoResource<T> {
            return RepoResource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data: T): RepoResource<T> {
            return RepoResource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T): RepoResource<T> {
            return RepoResource(Status.LOADING, data, null)
        }
    }

    enum class Status {
        SUCCESS, LOADING, ERROR
    }
}