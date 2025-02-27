package com.ph.testtt.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomStringDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(randomString: RandomStringEntity)

    @Query("SELECT * FROM random_strings ORDER BY id DESC")
    fun getAllStrings(): Flow<List<RandomStringEntity>>

    @Query("DELETE FROM random_strings WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM random_strings")
    suspend fun deleteAll()


    @Query("UPDATE RANDOM_STRINGS SET isFavourite = :isFav where id = :id")
    fun updateFavourite(id: Int, isFav: Boolean)

    @Query("Select * From random_strings where isFavourite==1")
    fun showAllFavourite() : Flow<List<RandomStringEntity>>
}
