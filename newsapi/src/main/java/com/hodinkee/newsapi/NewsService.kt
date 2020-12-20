package com.hodinkee.newsapi

import com.hodinkee.newsapi.model.NewsResponseJson
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("everything?qInTitle=watches&apiKey=$API_KEY")
    suspend fun fetchNews(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NewsResponseJson
}