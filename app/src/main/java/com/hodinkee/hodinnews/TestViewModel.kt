package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodinkee.newsapi.NewsService
import kotlinx.coroutines.launch

class TestViewModel @ViewModelInject constructor(
    private val newsService: NewsService
) : ViewModel() {
    fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = newsService.fetchNews(20, 1)
                println(response)
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}