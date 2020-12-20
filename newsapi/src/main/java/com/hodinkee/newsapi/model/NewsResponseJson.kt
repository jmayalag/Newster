package com.hodinkee.newsapi.model

import java.util.*


data class ArticleJson(
    val source: SourceJson,
    val author: String,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date,
    val content: String,
)

data class SourceJson(
    val id: String?,
    val name: String
)

data class NewsResponseJson(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleJson>
)
