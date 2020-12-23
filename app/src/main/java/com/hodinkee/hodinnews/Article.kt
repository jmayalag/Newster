package com.hodinkee.hodinnews

import android.os.Parcelable
import com.hodinkee.hodinnews.news.data.ArticleDto
import com.hodinkee.hodinnews.news.data.Category
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Article(
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