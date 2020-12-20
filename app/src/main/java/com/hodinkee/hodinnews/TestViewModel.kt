package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hodinkee.hodinnews.news.ArticlesPagingSource
import com.hodinkee.hodinnews.news.data.ArticleRepository

class TestViewModel @ViewModelInject constructor(
    private val articlesRepository: ArticleRepository
) : ViewModel() {

    val articlesFlow = Pager(PagingConfig(pageSize = 20)) {
        ArticlesPagingSource(articlesRepository)
    }.flow.cachedIn(viewModelScope)
}