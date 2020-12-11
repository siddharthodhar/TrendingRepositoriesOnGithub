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
        fun <T> success(@Nullable data: T): RepoResource<T> {
            return RepoResource(Status.SUCCESS, data, null)
        }

        fun <T> error(@NonNull message: String?, @Nullable data: T): RepoResource<T> {
            return RepoResource(Status.ERROR, data, message)
        }

        fun <T> loading(@Nullable data: T): RepoResource<T> {
            return RepoResource(Status.LOADING, data, null)
        }
    }

    enum class Status {
        SUCCESS, LOADING, ERROR
    }
}