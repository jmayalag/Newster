package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodinkee.newsapi.NewsService
import kotlinx.coroutines.launch

class TestViewModel @ViewModelInject constructor() : ViewModel() {
    fun fetchNews(service: NewsService) {
        viewModelScope.launch {
            try {
                val response = service.fetchNews(20, 1)
                println(response)
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}