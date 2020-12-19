package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodinkee.newsapi.NewsService
import kotlinx.coroutines.launch
import timber.log.Timber

class TestViewModel @ViewModelInject constructor(
    private val newsService: NewsService
) : ViewModel() {
    fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = newsService.fetchNews(20, 1)
                Timber.d("News total: %d, size: %d", response.totalResults, response.articles.size)
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch news")
            }
        }
    }
}