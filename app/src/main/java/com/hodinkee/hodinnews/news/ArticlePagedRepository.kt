package com.hodinkee.hodinnews.news

import androidx.paging.PagingData
import com.hodinkee.hodinnews.news.data.ArticleDto
import kotlinx.coroutines.flow.Flow

interface ArticlePagedRepository {
    fun fetchNews(pageSize: Int): Flow<PagingData<ArticleDto>>
}