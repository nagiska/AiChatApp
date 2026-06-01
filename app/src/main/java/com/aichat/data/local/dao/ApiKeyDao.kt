package com.aichat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aichat.data.local.entity.ApiKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiKeyDao {
    @Query("SELECT * FROM api_keys")
    fun getAll(): Flow<List<ApiKeyEntity>>

    @Query("SELECT * FROM api_keys WHERE provider = :provider LIMIT 1")
    fun getByProvider(provider: String): Flow<ApiKeyEntity?>

    @Query("SELECT * FROM api_keys WHERE provider = :provider AND isDefault = 1 LIMIT 1")
    suspend fun getDefaultByProvider(provider: String): ApiKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ApiKeyEntity)

    @Query("DELETE FROM api_keys WHERE id = :id")
    suspend fun deleteById(id: String)
}
