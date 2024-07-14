package com.example.basicofkotlin.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StrageDao {
    @Query("SELECT * FROM strage ORDER BY id ASC")
    fun getAll(): Flow<List<Strage>>

    @Query("SELECT * FROM strage WHERE id = :strageId")
    fun getStrageById(strageId: Int?): Flow<Strage>

    @Query("SELECT * FROM strage WHERE uri = :uri")
    suspend fun getStrageByUri(uri: String): Strage?

    @Query("SELECT * FROM strage WHERE photoId = :photoId")
    fun getEntriesByPhotoId(photoId: Int): Flow<List<Strage>>

    @Insert
    suspend fun insert(strage: Strage)

    @Delete
    suspend fun delete(strage: Strage)
}