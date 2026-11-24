package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DuaDao {
    @Query("SELECT * FROM duas")
    fun getAllDuas(): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE category = :category")
    fun getDuasByCategory(category: String): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE id = :id")
    suspend fun getDuaById(id: String): DuaEntity?

    @Query("SELECT * FROM duas WHERE isFavorite = 1")
    fun getFavoriteDuas(): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE collectionName = :collectionName")
    fun getDuasByCollection(collectionName: String): Flow<List<DuaEntity>>

    @Query("""
        SELECT * FROM duas WHERE 
        title LIKE '%' || :query || '%' OR 
        arabic LIKE '%' || :query || '%' OR 
        banglaPronunciation LIKE '%' || :query || '%' OR 
        banglaMeaning LIKE '%' || :query || '%' OR 
        englishMeaning LIKE '%' || :query || '%' OR 
        tags LIKE '%' || :query || '%'
    """)
    fun searchDuas(query: String): Flow<List<DuaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDua(dua: DuaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(duas: List<DuaEntity>)

    @Update
    suspend fun updateDua(dua: DuaEntity)

    @Query("UPDATE duas SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE duas SET collectionName = :collectionName WHERE id = :id")
    suspend fun updateCollection(id: String, collectionName: String?)

    @Delete
    suspend fun deleteDua(dua: DuaEntity)
}
