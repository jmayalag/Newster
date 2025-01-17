package com.incursio.newster.data.remote

import androidx.paging.PagingSource
import com.incursio.newster.data.local.ArticleDto
import com.incursio.newster.data.toDto
import com.incursio.newsapi.NEWS_START_PAGE
import com.incursio.newsapi.NewsService
import retrofit2.HttpException

class ArticlesPagingSource constructor(
    private val newsService: NewsService
) : PagingSource<Int, ArticleDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleDto> {
        return try {
            val pageNumber = params.key ?: NEWS_START_PAGE
            val response = newsService.fetchNews(params.loadSize, pageNumber)
            val nextPage = if (response.articles.isEmpty()) null else pageNumber + 1

            LoadResult.Page(
                data = response.articles.map { it.toDto() },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: HttpException) {
            when (e.code()) {
                426 -> LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = null
                )
                else -> LoadResult.Error(e)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}