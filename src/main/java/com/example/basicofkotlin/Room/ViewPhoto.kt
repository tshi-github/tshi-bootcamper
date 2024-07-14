package com.example.basicofkotlin.Room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViewPhoto(private val photoDao: PhotoDao) : ViewModel() {
    var description by mutableStateOf("")

}
