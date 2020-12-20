package com.hodinkee.hodinnews

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hodinkee.hodinnews.news.data.ArticleDao
import com.hodinkee.hodinnews.news.data.ArticleDto

@Database(entities = [ArticleDto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}