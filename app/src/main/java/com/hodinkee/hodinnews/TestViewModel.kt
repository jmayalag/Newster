package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hodinkee.hodinnews.news.ArticleRemoteMediator
import com.hodinkee.hodinnews.news.ArticleRepository
import com.hodinkee.hodinnews.news.ArticlesPagingSource
import com.hodinkee.newsapi.NewsService
import javax.inject.Inject

class TestViewModel @ViewModelInject @Inject constructor(
    private val articlesRepository: ArticleRepository,
    private val newsService: NewsService,
    private val db: AppDatabase
) : ViewModel() {

    val articlesFlow = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
        ArticlesPagingSource(articlesRepository)
    }.flow.cachedIn(viewModelScope)

    @ExperimentalPagingApi
    val articlesDbFlow = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = ArticleRemoteMediator(db, newsService),
        pagingSourceFactory = { db.articleDao().pagingSource() }
    ).flow.cachedIn(viewModelScope)
}