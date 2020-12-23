package com.incursio.newsapi

import com.incursio.newsapi.NewsService
import com.incursio.newsapi.provideMoshiConverter
import com.incursio.newsapi.provideOkHttpClientBuilder
import com.incursio.newsapi.provideRetrofitBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(RemoteApiTest::class)
class TestRemoteFetch {
    private val retrofit by lazy {
        val moshi = provideMoshiConverter()
        val okHttpClient = provideOkHttpClientBuilder()
            //.addInterceptor(PrintLoggingInterceptor())
            .build()
        val retrofit = provideRetrofitBuilder(moshi, okHttpClient).build()
        retrofit
    }

    private val newsService by lazy { retrofit.create(NewsService::class.java) }

    @Test
    fun `Test parsing`(): Unit = runBlocking {
        try {
            val result = newsService.fetchNews(20, 1)
            println("Total: ${result.totalResults}, size: ${result.articles.size}")
            result.articles.forEach {
                println("${it.title} (${it.publishedAt})")
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}