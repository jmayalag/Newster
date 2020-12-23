package com.incursio.newster

import android.os.Parcelable
import com.incursio.newster.news.data.ArticleDto
import com.incursio.newster.news.data.Category
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Article(
    val id: String,
    val source: String,
    val author: String,
    val title: String,
    val url: String,
    val description: String?,
    val urlToImage: String?,
    val content: String,
    val publishedAt: Date,
    val category: Category
) : Parcelable

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