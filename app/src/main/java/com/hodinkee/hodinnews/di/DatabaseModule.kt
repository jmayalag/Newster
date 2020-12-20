package com.hodinkee.hodinnews.di

import android.content.Context
import androidx.room.Room
import com.hodinkee.hodinnews.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "hokineews"
        ).build()
    }

    @Provides
    fun provideArticleDao(appDatabase: AppDatabase) = appDatabase.articleDao()
}