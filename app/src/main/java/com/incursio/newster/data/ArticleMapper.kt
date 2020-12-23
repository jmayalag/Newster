package com.incursio.newster.data

import com.incursio.newsapi.model.ArticleJson
import com.incursio.newsapi.model.SourceJson
import com.incursio.newster.data.local.ArticleDto
import com.incursio.newster.data.local.Category
import com.incursio.newster.view.Article

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

fun ArticleDto.toView() = Article(
    id = id,
    source = source,
    author = author,
    title = title,
    url = url,
    description = description,
    urlToImage = urlToImage,
    content = content,
    publishedAt = publishedAt,
    category = category
)

fun Article.toDto() = ArticleDto(
    id = id,
    source = source,
    author = author,
    title = title,
    url = url,
    description = description,
    urlToImage = urlToImage,
    content = content,
    publishedAt = publishedAt,
    category = category
)