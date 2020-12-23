package com.incursio.newster.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.incursio.newster.data.local.AppDatabase
import com.incursio.newster.data.local.ArticleDto
import com.incursio.newster.data.local.Category
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