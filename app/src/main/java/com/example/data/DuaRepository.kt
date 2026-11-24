package com.example.data

import kotlinx.coroutines.flow.Flow

class DuaRepository(private val duaDao: DuaDao) {
    val allDuas: Flow<List<DuaEntity>> = duaDao.getAllDuas()
    val favoriteDuas: Flow<List<DuaEntity>> = duaDao.getFavoriteDuas()

    fun getDuasByCategory(category: String): Flow<List<DuaEntity>> {
        return duaDao.getDuasByCategory(category)
    }

    fun getDuasByCollection(collectionName: String): Flow<List<DuaEntity>> {
        return duaDao.getDuasByCollection(collectionName)
    }

    fun searchDuas(query: String): Flow<List<DuaEntity>> {
        return duaDao.searchDuas(query)
    }

    suspend fun getDuaById(id: String): DuaEntity? {
        return duaDao.getDuaById(id)
    }

    suspend fun insertDua(dua: DuaEntity) {
        duaDao.insertDua(dua)
    }

    suspend fun updateFavorite(id: String, isFavorite: Boolean) {
        duaDao.updateFavorite(id, isFavorite)
    }

    suspend fun updateCollection(id: String, collectionName: String?) {
        duaDao.updateCollection(id, collectionName)
    }

    suspend fun deleteDua(dua: DuaEntity) {
        duaDao.deleteDua(dua)
    }
}
