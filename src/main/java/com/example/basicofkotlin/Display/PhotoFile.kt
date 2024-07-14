package com.example.basicofkotlin.Display

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.basicofkotlin.Display.Greetings.Greetings
import com.example.basicofkotlin.Room.PhotoDao
import com.example.basicofkotlin.Room.Strage
import com.example.basicofkotlin.Room.StrageDao
import kotlinx.coroutines.launch

@Composable
fun PhotoFile(
    navController: NavHostController,
    photoDao: PhotoDao,
    strageDao: StrageDao,
    photoId: Int?
) {
    val strageList by strageDao.getEntriesByPhotoId(photoId ?: 0).collectAsState(initial = emptyList())
    val photoUris = remember { mutableStateListOf<Uri>() }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null && !photoUris.contains(uri)) {
            photoUris.add(uri)
            coroutineScope.launch {
                if (uri.toString().isNotEmpty() && photoId != null) {
                    strageDao.insert(Strage(photoId = photoId, uri = uri.toString(), description = "Uploaded from PhotoFile"))
                }
            }
        }
    }

    strageList.forEach { strage ->
        val uri = Uri.parse(strage.uri)
        if (!photoUris.contains(uri)) {
            photoUris.add(uri)
        }
        Log.d("PhotoFile", "Stored URI: ${strage.uri}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(text = "Load Image", color = Color.White)
        }
        PhotoGrid(photoUris = photoUris, strageDao = strageDao, photoUrisList = photoUris)
    }
}

@Composable
fun PhotoGrid(photoUris: List<Uri?>, strageDao: StrageDao, photoUrisList: MutableList<Uri>) {
    val coroutineScope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(photoUris.size) { index ->
            val uri = photoUris[index]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Image $index",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val strageEntry = strageDao.getStrageByUri(uri.toString())
                            if (strageEntry != null) {
                                strageDao.delete(strageEntry)
                                photoUrisList.remove(uri)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(text = "Delete", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(photoDao: PhotoDao, strageDao: StrageDao) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "greetings") {
        composable("greetings") {
            Greetings(
                photoDao = photoDao,
                strageDao = strageDao,
                navController = navController
            )
        }
        composable("file/{photoId}") { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId")?.toIntOrNull()
            PhotoFile(
                photoDao = photoDao,
                strageDao = strageDao,
                photoId = photoId,
                navController = navController
            )
        }
    }
}