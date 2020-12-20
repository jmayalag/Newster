package com.hodinkee.hodinnews.news

import androidx.paging.PagingSource
import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.model.ArticleJson
import retrofit2.HttpException
import kotlin.math.ceil

class ArticlesPagingSource constructor(
    private val newsService: NewsService
) : PagingSource<Int, ArticleJson>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleJson> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = newsService.fetchNews(params.loadSize, nextPageNumber)
            val total = response.totalResults
            val totalPages = ceil(total.toFloat() / params.loadSize).toInt()
            val nextPage = if (nextPageNumber < totalPages) nextPageNumber + 1 else null

            LoadResult.Page(
                data = response.articles,
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