package com.hodinkee.hodinnews.news.data

import androidx.paging.PagingSource
import androidx.room.*


@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    suspend fun getAll(): List<ArticleDto>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): ArticleDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg articles: ArticleDto)

    @Update
    suspend fun update(vararg articles: ArticleDto)

    @Delete
    suspend fun delete(vararg articles: ArticleDto)

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun pagingSource(): PagingSource<Int, ArticleDto>

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}