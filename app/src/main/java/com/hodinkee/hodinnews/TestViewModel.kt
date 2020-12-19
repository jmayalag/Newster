package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.model.ArticleJson
import kotlinx.coroutines.launch
import timber.log.Timber

class TestViewModel @ViewModelInject constructor(
    private val newsService: NewsService
) : ViewModel() {
    private val _articles = MutableLiveData(listOf<ArticleJson>())
    val articles = _articles.readOnly

    val articlesString = Transformations.map(articles) {
        if (it == null || it.isEmpty()) {
            "No Articles found"
        } else {
            it.joinToString("\n") { a -> a.title }
        }
    }

    fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = newsService.fetchNews(20, 1)
                Timber.d("News total: %d, size: %d", response.totalResults, response.articles.size)
                _articles.value = response.articles
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch news")
            }
        }
    }
}