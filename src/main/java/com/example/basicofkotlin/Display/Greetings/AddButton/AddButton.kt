package com.example.basicofkotlin.Display.Greetings.AddButton

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.basicofkotlin.Display.GreetingsData
import com.example.basicofkotlin.Room.PhotoDao
import com.example.basicofkotlin.Room.ViewPhoto

@Composable
fun AddButton(photoDao: PhotoDao, viewPhoto: ViewPhoto = ViewPhoto(photoDao)) {
    val scope = rememberCoroutineScope()
    var isDialogOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { isDialogOpen = true },
            contentColor = Color.Black,
            modifier = Modifier
                .padding(end = 36.dp, bottom = 75.dp)
                .align(Alignment.BottomEnd)
                .size(72.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add"
            )
        }

        if (isDialogOpen) {

            val greetingsData = GreetingsData(
                viewPhoto = viewPhoto,
                photoDao = photoDao,
                isDialogOpen = isDialogOpen,
                onDismissRequest = { isOpen -> isDialogOpen = isOpen },
                scope = scope,
            )

            AddText(greetingsData)

        }
    }
}