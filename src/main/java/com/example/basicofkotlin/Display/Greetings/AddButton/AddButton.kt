package com.example.basicofkotlin.Display.Greetings.AddButton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.basicofkotlin.Display.Greetings.GreetingsData
import com.example.basicofkotlin.Room.AppDatabase
import com.example.basicofkotlin.Room.ViewPhoto

@Composable
fun AddButton(db: AppDatabase, viewPhoto: ViewPhoto = ViewPhoto(db.photoDao())) {
    val scope = rememberCoroutineScope()
    var isDialogOpen by remember { mutableStateOf(false) }

    Button(
        onClick = { isDialogOpen = true },
        modifier = Modifier
            .fillMaxWidth()
        ){
        Text(
            text = "追加",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
    if (isDialogOpen) {
        val greetingsData = GreetingsData(
            viewPhoto = viewPhoto,
            photoDao = db.photoDao(),
            isDialogOpen = isDialogOpen,
            onDismissRequest = { isOpen -> isDialogOpen = isOpen },
            scope = scope
        )
        AddText(greetingsData)
    }
}