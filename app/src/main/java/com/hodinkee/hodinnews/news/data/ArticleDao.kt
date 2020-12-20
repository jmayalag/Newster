package com.hodinkee.hodinnews.news.data

import androidx.room.*


@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAll(): List<ArticleDto>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun findById(id: String): ArticleDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg articles: ArticleDto)

    @Update
    fun update(vararg articles: ArticleDto)

    @Delete
    fun delete(vararg articles: ArticleDto)
}