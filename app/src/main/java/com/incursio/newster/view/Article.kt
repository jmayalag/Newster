package com.incursio.newster.view

import android.os.Parcelable
import com.incursio.newster.data.local.Category
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