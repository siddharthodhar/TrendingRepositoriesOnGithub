package com.siddhartho.trendingrepositories.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "repo_details")
data class RepoDetail(
    @Expose
    var author: String,
    @Expose
    var name: String,
    @Expose
    var avatar: String,
    @Expose
    var url: String,
    @Expose
    var description: String,
    @Expose
    var language: String,
    @Expose
    var languageColor: String,
    @Expose
    var stars: String
) {

    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    var repoId: Int? = null
}