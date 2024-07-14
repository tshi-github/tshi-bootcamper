package com.example.basicofkotlin.Display.Greetings.AddButton

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.basicofkotlin.Display.Greetings.GreetingsData
import com.example.basicofkotlin.Room.Photo
import kotlinx.coroutines.launch

@Composable
fun AddText(greetingsData: GreetingsData){

    AlertDialog(
        onDismissRequest = { greetingsData.onDismissRequest(false)},
        title = { Text(text = "車両の名前を入力") },
        text = {
            OutlinedTextField(
                value = greetingsData.viewPhoto.description,
                onValueChange = { greetingsData.viewPhoto.description = it },
                label = { Text("形式/編成番号") }
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
                Text("追加")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                greetingsData.onDismissRequest(false)
            }
            ) { Text("キャンセル") }
        }
    )
}
