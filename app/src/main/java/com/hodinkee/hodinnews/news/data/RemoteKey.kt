package com.hodinkee.hodinnews.news.data

import androidx.room.*

enum class RemoteKeyType {
    ARTICLE
}

@Entity(tableName = "remote_keys")
data class RemoteKeyDto(
    @PrimaryKey
    val type: String,
    val nextPageKey: Int?
)

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg keys: RemoteKeyDto)

    @Query("SELECT * FROM remote_keys WHERE type = :type LIMIT 1")
    suspend fun remoteKeyByType(type: String): RemoteKeyDto?

    @Query("DELETE FROM remote_keys WHERE type = :type")
    suspend fun deleteByType(type: String)
}