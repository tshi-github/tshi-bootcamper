package com.example.basicofkotlin.Display.Greetings.AddButton

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.basicofkotlin.Display.GreetingsData
import com.example.basicofkotlin.Room.Photo
import kotlinx.coroutines.launch

@Composable
fun AddText(greetingsData: GreetingsData){
    var text by remember{mutableStateOf("")}

    AlertDialog(
        onDismissRequest = { greetingsData.onDismissRequest(false)},
        title = { Text(text = "Add New Item") },
        text = {
            OutlinedTextField(
                value = greetingsData.viewPhoto.description,
                onValueChange = { greetingsData.viewPhoto.description = it },
                label = { Text("Item Text") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    greetingsData.scope.launch {
                        if(greetingsData.viewPhoto.description.isNotBlank()){
                            val photo = Photo(description = greetingsData.viewPhoto.description)
                            greetingsData.photoDao.insert(photo)
                            greetingsData.viewPhoto.description = ""
                            greetingsData.onDismissRequest(false)
                        }
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                greetingsData.onDismissRequest(true)
            }
            ) { Text("Cancel") }
        }
    )
}