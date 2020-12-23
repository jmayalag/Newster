package com.incursio.newster.news.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.incursio.newster.AppDatabase
import com.incursio.newster.news.ArticlePagedRepository
import com.incursio.newster.news.data.ArticleDto
import com.incursio.newster.news.data.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class LocalArticleDbPagedRepository @Inject constructor(
    private val db: AppDatabase,
) : ArticlePagedRepository {
    override fun fetchNews(pageSize: Int): Flow<PagingData<ArticleDto>> =
        Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { db.articleDao().pagingSource(Category.LOCAL) }
        ).flow
}