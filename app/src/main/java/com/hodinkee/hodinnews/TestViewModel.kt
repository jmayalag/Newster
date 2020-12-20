package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hodinkee.hodinnews.news.RemotePagingSource
import com.hodinkee.newsapi.NewsService

class TestViewModel @ViewModelInject constructor(
    private val newsService: NewsService
) : ViewModel() {

    val articlesFlow = Pager(PagingConfig(pageSize = 20)) {
        RemotePagingSource(newsService)
    }.flow.cachedIn(viewModelScope)
}