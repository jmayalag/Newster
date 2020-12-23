package com.hodinkee.hodinnews.news.data

import androidx.room.*

enum class Category {
    REMOTE,
    LOCAL
}

@Entity(tableName = "remote_keys")
data class RemoteKeyDto(
    @PrimaryKey
    val type: Category,
    val nextPageKey: Int?
)

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg keys: RemoteKeyDto)

    @Query("SELECT * FROM remote_keys WHERE type = :category LIMIT 1")
    suspend fun remoteKeyByType(category: Category): RemoteKeyDto?

    @Query("DELETE FROM remote_keys WHERE type = :type")
    suspend fun deleteByType(type: String)
}