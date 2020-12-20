package com.hodinkee.hodinnews

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hodinkee.hodinnews.news.data.ArticleDao
import com.hodinkee.hodinnews.news.data.ArticleDto
import com.hodinkee.hodinnews.news.data.Converters

// TODO: Export schema
@Database(entities = [ArticleDto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}