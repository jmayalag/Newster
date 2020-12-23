package com.incursio.newster.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.incursio.newster.data.datasource.ArticlePagedRepository
import com.incursio.newster.data.local.ArticleDto
import com.incursio.newsapi.NewsService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRemotePagedRepository @Inject constructor(
    private val newsService: NewsService
) : ArticlePagedRepository {
    override fun fetchNews(pageSize: Int): Flow<PagingData<ArticleDto>> =
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            ArticlesPagingSource(newsService)
        }.flow
}