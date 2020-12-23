package com.incursio.newster.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "articles")
data class ArticleDto(
 @PrimaryKey val id: String,
 val source: String,
 val author: String,
 val title: String,
 val url: String,
 val description: String?,
 val urlToImage: String?,
 val content: String,
 val publishedAt: Date,
 val category: Category
)