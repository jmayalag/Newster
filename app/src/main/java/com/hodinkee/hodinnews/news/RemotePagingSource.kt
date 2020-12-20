package com.hodinkee.hodinnews.news

import androidx.paging.PagingSource
import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.model.ArticleJson
import javax.inject.Inject
import kotlin.math.ceil

class RemotePagingSource constructor(
    private val newsService: NewsService
) : PagingSource<Int, ArticleJson>() {
    private val pageSize = 20

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleJson> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = newsService.fetchNews(params.loadSize, nextPageNumber)
            val total = response.totalResults
            val totalPages = ceil(total.toFloat() / pageSize).toInt()
            val nextPage = if (nextPageNumber < totalPages) nextPageNumber + 1 else null

            LoadResult.Page(
                data = response.articles,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}