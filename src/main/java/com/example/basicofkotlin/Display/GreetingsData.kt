package com.example.basicofkotlin.Display

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.example.basicofkotlin.Room.PhotoDao
import com.example.basicofkotlin.Room.ViewPhoto
import kotlinx.coroutines.CoroutineScope

data class GreetingsData(
    val viewPhoto: ViewPhoto,
    val photoDao: PhotoDao,
    val isDialogOpen: Boolean,
    val onDismissRequest: (Boolean) -> Unit,
    val scope: CoroutineScope,
)