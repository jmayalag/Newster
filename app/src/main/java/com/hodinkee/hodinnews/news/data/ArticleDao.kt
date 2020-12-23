package com.hodinkee.hodinnews.news.data

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    suspend fun getAll(): List<ArticleDto>

    @Query("SELECT * FROM articles WHERE category = :category")
    suspend fun findByCategory(category: Category): List<ArticleDto>

    @Query("SELECT * FROM articles where category = :category ORDER BY publishedAt DESC")
    fun pagingSource(category: Category): PagingSource<Int, ArticleDto>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun findByIdFlow(id: String): Flow<ArticleDto?>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): ArticleDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg articles: ArticleDto)

    @Update
    suspend fun update(vararg articles: ArticleDto)

    @Delete
    suspend fun delete(vararg articles: ArticleDto): Int

    @Query("DELETE FROM articles WHERE category = :category")
    suspend fun clearAll(category: Category)
}