package com.incursio.newster.news

import androidx.paging.PagingData
import com.incursio.newster.news.data.ArticleDto
import kotlinx.coroutines.flow.Flow

interface ArticlePagedRepository {
    fun fetchNews(pageSize: Int): Flow<PagingData<ArticleDto>>
}