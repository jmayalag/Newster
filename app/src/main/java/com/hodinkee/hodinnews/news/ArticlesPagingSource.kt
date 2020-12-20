package com.hodinkee.hodinnews.news

import androidx.paging.PagingSource
import com.hodinkee.hodinnews.news.data.ArticleRepository
import com.hodinkee.newsapi.model.ArticleJson
import retrofit2.HttpException

private const val START_PAGE = 1

class ArticlesPagingSource constructor(
    private val articleRepository: ArticleRepository
) : PagingSource<Int, ArticleJson>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleJson> {
        return try {
            val pageNumber = params.key ?: START_PAGE
            val response = articleRepository.fetchNews(params.loadSize, pageNumber)
            val nextPage = if (response.articles.isEmpty()) null else pageNumber + 1

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