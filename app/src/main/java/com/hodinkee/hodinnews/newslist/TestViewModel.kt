package com.hodinkee.hodinnews.newslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.hodinkee.hodinnews.news.db.ArticleDbPagedRepository
import com.hodinkee.hodinnews.news.remote.ArticleRemotePagedRepository
import javax.inject.Inject

@ExperimentalPagingApi
class TestViewModel @ViewModelInject @Inject constructor(
    articleRemoteRepository: ArticleRemotePagedRepository,
    articleDbPagedRepository: ArticleDbPagedRepository,
) : ViewModel() {

    val articlesFlow = articleRemoteRepository.fetchNews(20).cachedIn(viewModelScope)

    @ExperimentalPagingApi
    val articlesDbFlow = articleDbPagedRepository.fetchNews(20).cachedIn(viewModelScope)
}