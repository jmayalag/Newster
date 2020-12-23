package com.incursio.newster.news.data

import com.incursio.newsapi.model.ArticleJson
import com.incursio.newsapi.model.SourceJson

fun ArticleJson.toDto() = ArticleDto(
    id = url.hashCode().toString(),
    source = source.name,
    author = author ?: "",
    title = title,
    url = url,
    description = description,
    urlToImage = urlToImage,
    content = content ?: "",
    publishedAt = publishedAt,
    category = Category.REMOTE
)

fun ArticleDto.toJson() = ArticleJson(
    source = SourceJson(id = null, name = this.source),
    author = author,
    title = title,
    url = url,
    description = description,
    urlToImage = urlToImage,
    content = content,
    publishedAt = publishedAt
)