package com.hodinkee.hodinnews.news

import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.model.NewsResponseJson
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val newsService: NewsService
) {
    suspend fun fetchNews(pageSize: Int, page: Int): NewsResponseJson {
        return newsService.fetchNews(pageSize, page)
    }
}