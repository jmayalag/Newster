package com.hodinkee.hodinnews.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hodinkee.hodinnews.databinding.ArticleItemBinding
import com.hodinkee.hodinnews.news.data.ArticleDto
import com.hodinkee.hodinnews.util.DateFormat

class ArticlesAdapter(
    diffCallback: DiffUtil.ItemCallback<ArticleDto>,
    private val onClick: (vh: ArticleDto) -> Unit = {}
) : PagingDataAdapter<ArticleDto, ArticlesAdapter.ArticleViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ArticleViewHolder private constructor(
        private val binding: ArticleItemBinding,
        private val onClick: (vh: ArticleDto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var item: ArticleDto? = null

        init {
            binding.root.setOnClickListener {
                this.item?.let { onClick(it) }
            }
        }

        fun bind(item: ArticleDto?) {
            this.item = item
            binding.title.text = item?.title ?: "Loading..."
            binding.source.text =
                item?.source?.let { if (it.isEmpty()) "Unknown Source" else it }
                    ?: "Loading..."

            binding.date.text = item?.publishedAt?.let { DateFormat.ymd(it) } ?: "Loading..."
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (ArticleDto) -> Unit): ArticleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ArticleItemBinding.inflate(layoutInflater, parent, false)
                return ArticleViewHolder(binding, onClick)
            }
        }
    }

    object ArticleComparator : DiffUtil.ItemCallback<ArticleDto>() {
        override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
            return oldItem == newItem
        }
    }
}