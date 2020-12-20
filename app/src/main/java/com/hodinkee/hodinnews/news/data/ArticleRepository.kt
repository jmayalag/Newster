package com.hodinkee.hodinnews.news.data

import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.model.NewsResponseJson
import timber.log.Timber
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleDao: ArticleDao,
    private val newsService: NewsService
) {
    suspend fun fetchNews(pageSize: Int, page: Int): NewsResponseJson {
        val response = newsService.fetchNews(pageSize, page)

        try {
            val dtos = response.articles.map { it.toDto() }
            articleDao.insert(*dtos.toTypedArray())
        } catch (e: Exception) {
            Timber.e(e, "Error saving to db")
        }

        return response
    }
}