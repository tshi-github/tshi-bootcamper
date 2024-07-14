package com.example.basicofkotlin.Room;

import androidx.room.Dao;
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query;
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao{
    @Query("SELECT * FROM photo ORDER BY uid ASC")
    fun getAll(): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE uid = :photoId")
    fun getPhotoById(photoId: Int?): Flow<Photo>

    @Insert
    suspend fun insert(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)
}