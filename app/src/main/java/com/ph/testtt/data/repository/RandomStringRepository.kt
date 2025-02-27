package com.ph.testtt.data.repository

import com.ph.testtt.data.room.RandomStringDao
import com.ph.testtt.data.room.RandomStringEntity
import kotlinx.coroutines.flow.Flow

class RandomStringRepository(private val dao: RandomStringDao) {
    val allStrings: Flow<List<RandomStringEntity>> = dao.getAllStrings()

    suspend fun insert(randomString: RandomStringEntity) {
        dao.insert(randomString)
    }

    suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun makeFavourite(id: Int) {
        dao.updateFavourite(id)
    }

    fun showAllFav(): Flow<List<RandomStringEntity>> {
        return dao.showAllFavourite()
    }
}
