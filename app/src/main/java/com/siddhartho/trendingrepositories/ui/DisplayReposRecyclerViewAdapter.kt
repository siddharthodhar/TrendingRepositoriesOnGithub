package com.siddhartho.trendingrepositories.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.siddhartho.trendingrepositories.R
import com.siddhartho.trendingrepositories.databinding.RepoListItemBinding
import com.siddhartho.trendingrepositories.di.ui.ActivityScope
import com.siddhartho.trendingrepositories.model.RepoDetail
import javax.inject.Inject

@ActivityScope
class DisplayReposRecyclerViewAdapter @Inject constructor() :
    RecyclerView.Adapter<DisplayReposRecyclerViewAdapter.DisplayReposViewHolder>() {

    private var mRepoDetailList = ArrayList<RepoDetail>()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayReposViewHolder {
        Log.d(TAG, "onCreateViewHolder() called with: parent = $parent, viewType = $viewType")
        return DisplayReposViewHolder(
            RepoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DisplayReposViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() called with: holder = $holder, position = $position")

        holder.repoListItemBinding.apply {
            requestManager
                .load(mRepoDetailList[position].avatar)
                .into(imageViewAvatar)
            textViewAuthor.text = mRepoDetailList[position].author
            textViewName.text = mRepoDetailList[position].name
            textViewDescription.text = mRepoDetailList[position].description
            imageViewLanguage.setColorFilter(Color.parseColor(mRepoDetailList[position].languageColor))
            textViewLanguage.text = mRepoDetailList[position].language
            textViewStars.text = mRepoDetailList[position].stars
            if (position != mRepoDetailList.size - 1) {
                imageViewDivider.setImageResource(R.drawable.divider_recycler_view_dash)
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called")
        return mRepoDetailList.size
    }

    fun setRepoDetailList(repoDetailList: List<RepoDetail>) {
        mRepoDetailList = ArrayList(repoDetailList)
        notifyDataSetChanged()
    }

    class DisplayReposViewHolder(val repoListItemBinding: RepoListItemBinding) :
        RecyclerView.ViewHolder(repoListItemBinding.root)

    companion object {
        private const val TAG = "DisplayReposRVAdapter"
    }

}