package com.incursio.newster.data.datasource

import androidx.paging.PagingData
import com.incursio.newster.data.local.ArticleDto
import kotlinx.coroutines.flow.Flow

interface ArticlePagedRepository {
    fun fetchNews(pageSize: Int): Flow<PagingData<ArticleDto>>
}