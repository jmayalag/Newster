package com.hodinkee.newsapi.model


data class ArticleJson(
    val name: String
)

data class SourceJson(
    val id: String?,
    val name: String
)

data class NewsResponseJson(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleJson>,
    val source: SourceJson,
    val author: String,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String, // TODO: Convert to date
    val content: String,
)
