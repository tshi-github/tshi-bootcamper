package com.example.basicofkotlin.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val description: String = "",
)