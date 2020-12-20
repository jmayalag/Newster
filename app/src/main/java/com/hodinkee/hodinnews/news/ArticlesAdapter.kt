package com.hodinkee.hodinnews.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hodinkee.hodinnews.DateFormat
import com.hodinkee.hodinnews.databinding.ArticleItemBinding
import com.hodinkee.newsapi.model.ArticleJson

class ArticlesAdapter(diffCallback: DiffUtil.ItemCallback<ArticleJson>) :
    PagingDataAdapter<ArticleJson, ArticlesAdapter.ArticleViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ArticleViewHolder private constructor(
        private val binding: ArticleItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ArticleJson?) {
            binding.title.text = item?.title ?: "Loading..."
            binding.source.text =
                item?.source?.name?.let { if (it.isEmpty()) "Unknown Source" else it }
                    ?: "Loading..."

            binding.date.text = item?.publishedAt?.let { DateFormat.ymd(it) } ?: "Loading..."
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ArticleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ArticleItemBinding.inflate(layoutInflater, parent, false)
                return ArticleViewHolder(binding)
            }
        }
    }

    object ArticleComparator : DiffUtil.ItemCallback<ArticleJson>() {
        override fun areItemsTheSame(oldItem: ArticleJson, newItem: ArticleJson): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleJson, newItem: ArticleJson): Boolean {
            return oldItem == newItem
        }
    }
}