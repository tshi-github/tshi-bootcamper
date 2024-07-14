package com.example.basicofkotlin.Room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewPhoto(private val photoDao: PhotoDao) : ViewModel() {
    var description by mutableStateOf("")
    var flag by mutableStateOf(false)

    fun savePhoto(photo: Photo) {
        viewModelScope.launch {
            photoDao.insert(photo)
        }
    }
}
