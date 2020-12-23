package com.incursio.newster.news.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.incursio.newster.news.ArticlePagedRepository
import com.incursio.newster.news.data.ArticleDto
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