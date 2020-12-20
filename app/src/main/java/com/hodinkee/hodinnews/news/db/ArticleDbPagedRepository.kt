package com.hodinkee.hodinnews.news.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hodinkee.hodinnews.AppDatabase
import com.hodinkee.hodinnews.news.ArticlePagedRepository
import com.hodinkee.hodinnews.news.data.ArticleDto
import com.hodinkee.hodinnews.news.data.Category
import com.hodinkee.newsapi.NewsService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class ArticleDbPagedRepository @Inject constructor(
    private val db: AppDatabase,
    private val newsService: NewsService
) : ArticlePagedRepository {
    override fun fetchNews(pageSize: Int): Flow<PagingData<ArticleDto>> =
        Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = ArticleRemoteMediator(db, newsService),
            pagingSourceFactory = { db.articleDao().pagingSource(Category.REMOTE) }
        ).flow
}