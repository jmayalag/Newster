package com.hodinkee.hodinnews

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hodinkee.hodinnews.news.data.*

// TODO: Export schema
@Database(entities = [ArticleDto::class, RemoteKeyDto::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}