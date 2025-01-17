package com.incursio.newsapi

import com.incursio.newsapi.model.NewsResponseJson
import retrofit2.http.GET
import retrofit2.http.Query

const val NEWS_START_PAGE = 1

interface NewsService {
    @GET("everything?qInTitle=watches&apiKey=$API_KEY&sortBy=publishedAt")
    suspend fun fetchNews(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NewsResponseJson
}