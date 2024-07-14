package com.example.basicofkotlin.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Strage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri:String,
    val description: String = "",
    val photoId: Int
)
