package com.example.basicofkotlin.Display.Greetings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.basicofkotlin.Display.Greetings.AddButton.AddButton
import com.example.basicofkotlin.Room.Photo
import com.example.basicofkotlin.Room.PhotoDao
import com.example.basicofkotlin.Room.StrageDao
import com.example.basicokotlin.R
import kotlinx.coroutines.launch

@Composable
internal fun Greetings(
    modifier: Modifier = Modifier,
    photoDao: PhotoDao,
    strageDao: StrageDao,
    navController: NavHostController
) {
    val photoList by photoDao.getAll().collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = photoList) { photo ->
                Greeting(photo = photo, photoDao = photoDao, strageDao = strageDao, navController = navController)
            }
        }
        AddButton(photoDao = photoDao)
    }
}

@Composable
private fun Greeting(
    photo: Photo,
    photoDao: PhotoDao,
    strageDao: StrageDao,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = modifier.padding(vertical = 1.dp, horizontal = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxHeight()) {
                FloatingActionButton(
                    onClick = { navController.navigate("file/${photo.uid}") },
                    contentColor = Color.Black,
                    modifier = Modifier
                        .padding(end = 36.dp, top = 12.dp)
                        .align(Alignment.TopEnd)
                        .size(55.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Add")
                }

                IconButton(
                    onClick = {
                        scope.launch {
                            photoDao.delete(photo)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
                CardContent(photo.description)
            }
        }
    }
}


@Composable
private fun CardContent(name: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(modifier = Modifier.padding(12.dp)) {
        Image(
            painter = painterResource(R.drawable.e257),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
        }
    }
}