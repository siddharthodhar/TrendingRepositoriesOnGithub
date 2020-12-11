package com.siddhartho.trendingrepositories.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.siddhartho.trendingrepositories.R
import com.siddhartho.trendingrepositories.databinding.RepoListItemBinding
import com.siddhartho.trendingrepositories.di.ui.ActivityScope
import com.siddhartho.trendingrepositories.model.RepoDetail
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@ActivityScope
class DisplayReposRecyclerViewAdapter @Inject constructor() :
    RecyclerView.Adapter<DisplayReposRecyclerViewAdapter.DisplayReposViewHolder>(), Filterable {

    private var mRepoDetailList = ArrayList<RepoDetail>()
    private var mRepoDetailFullList = ArrayList<RepoDetail>()
    private var mOnItemClick: ((url: String, name: String, author: String) -> Unit)? = null

    @Inject
    lateinit var requestManager: RequestManager

    override fun getFilter(): Filter {
        Log.d(TAG, "getFilter() called")
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                Log.d(TAG, "performFiltering() called with: p0 = $p0")

                val filteredList = ArrayList<RepoDetail>()
                val filterResults = FilterResults()

                p0?.let { query ->
                    if (query.isEmpty()) {
                        filterResults.values = mRepoDetailFullList
                    } else {
                        for (repo in mRepoDetailFullList) {
                            if (repo.name.startsWith(p0.trim(), true)) {
                                filteredList.add(repo)
                            }
                        }
                        filterResults.values = filteredList
                    }
                    return filterResults
                }

                p0 ?: run {
                    filterResults.values = mRepoDetailFullList
                }

                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                Log.d(TAG, "publishResults() called with: p0 = $p0, p1 = $p1")

                val filteredList = p1?.values as List<RepoDetail>

                Log.d(TAG, "publishResults: $filteredList")

                for (repo in mRepoDetailFullList) {
                    if (!filteredList.contains(repo) && mRepoDetailList.contains(repo)) {
                        notifyItemRemoved(mRepoDetailList.indexOf(repo))
                        mRepoDetailList.remove(repo)
                    } else if (filteredList.contains(repo) && !mRepoDetailList.contains(repo)) {
                        mRepoDetailList.add(filteredList.indexOf(repo), repo)
                        notifyItemInserted(mRepoDetailList.indexOf(repo))
                    }
                }
            }
        }
    }

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
            } else {
                imageViewDivider.setImageDrawable(null)
            }
            root.setOnClickListener {
                mOnItemClick?.let { onItemClick ->
                    onItemClick(
                        mRepoDetailList[holder.adapterPosition].url,
                        mRepoDetailList[holder.adapterPosition].name,
                        mRepoDetailList[holder.adapterPosition].author
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called")
        return mRepoDetailList.size
    }

    fun setRepoDetailList(repoDetailList: List<RepoDetail>) {
        mRepoDetailList = ArrayList(repoDetailList)
        mRepoDetailFullList = ArrayList(repoDetailList)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClick: (url: String, name: String, author: String) -> Unit) {
        mOnItemClick = onItemClick
    }

    class DisplayReposViewHolder(val repoListItemBinding: RepoListItemBinding) :
        RecyclerView.ViewHolder(repoListItemBinding.root)

    companion object {
        private const val TAG = "DisplayReposRVAdapter"
    }

}