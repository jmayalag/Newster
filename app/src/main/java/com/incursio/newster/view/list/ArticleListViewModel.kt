package com.incursio.newster.view.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.incursio.newster.data.datasource.ArticleDbPagedRepository
import com.incursio.newster.data.datasource.LocalArticleDbPagedRepository
import com.incursio.newster.data.remote.ArticleRemotePagedRepository
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
class ArticleListViewModel @ViewModelInject @Inject constructor(
    articleRemoteRepository: ArticleRemotePagedRepository,
    articleDbPagedRepository: ArticleDbPagedRepository,
    localArticleDbPagedRepository: LocalArticleDbPagedRepository
) : ViewModel() {

    init {
        Timber.d("Created")
    }

    val articlesFlow = articleRemoteRepository.fetchNews(20).cachedIn(viewModelScope)

    @ExperimentalPagingApi
    val remoteArticlesFlow = articleDbPagedRepository.fetchNews(20).cachedIn(viewModelScope)

    val localArticlesFlow = localArticleDbPagedRepository.fetchNews(20).cachedIn(viewModelScope)
}