package com.hodinkee.hodinnews.news.data

import com.hodinkee.newsapi.model.ArticleJson
import com.hodinkee.newsapi.model.SourceJson

fun ArticleJson.toDto() = ArticleDto(
    id = url.hashCode().toString(),
    source = source.name,
    author = author ?: "",
    title = title,
    url = url,
    description = description,
    urlToImage = urlToImage,
    content = content ?: "",
    publishedAt = publishedAt
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