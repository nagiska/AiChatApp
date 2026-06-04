package com.aichat.data.db.dao

import androidx.room.*
import com.aichat.data.db.entity.Provider
import kotlinx.coroutines.flow.Flow

// 供应商数据访问对象
@Dao
interface ProviderDao {
    
    // 获取所有供应商
    @Query("SELECT * FROM providers ORDER BY createdAt DESC")
    fun getAllProviders(): Flow<List<Provider>>
    
    // 获取启用的供应商
    @Query("SELECT * FROM providers WHERE isEnabled = 1 ORDER BY createdAt DESC")
    fun getEnabledProviders(): Flow<List<Provider>>
    
    // 根据 ID 获取供应商
    @Query("SELECT * FROM providers WHERE id = :id")
    suspend fun getProviderById(id: String): Provider?
    
    // 根据类型获取供应商
    @Query("SELECT * FROM providers WHERE type = :type ORDER BY createdAt DESC")
    fun getProvidersByType(type: String): Flow<List<Provider>>
    
    // 插入供应商
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: Provider)
    
    // 更新供应商
    @Update
    suspend fun updateProvider(provider: Provider)
    
    // 删除供应商
    @Delete
    suspend fun deleteProvider(provider: Provider)
    
    // 根据 ID 删除供应商
    @Query("DELETE FROM providers WHERE id = :id")
    suspend fun deleteProviderById(id: String)
}
