package com.example.basicofkotlin.Display

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.basicofkotlin.Display.Greetings.AddButton.AddButton
import com.example.basicofkotlin.Display.HeadFooter.Footer
import com.example.basicofkotlin.Room.AppDatabase
import com.example.basicofkotlin.Room.Strage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun PhotoFile(
    db: AppDatabase,
    photoId: Int?,
) {
    val strageList by db.strageDao().getEntriesByPhotoId(photoId ?: 0).collectAsState(initial = emptyList())
    val photoUris = remember { mutableStateListOf<Uri>() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null && !photoUris.contains(uri)) {
            photoUris.add(uri)
            coroutineScope.launch {
                if (uri.toString().isNotEmpty() && photoId != null) {
                    val path = saveImageToInternalStorage(context = context, uri = uri)
                    db.strageDao().insert(Strage(photoId = photoId, uri = path, description = "Uploaded from PhotoFile"))
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.weight(1f)){PhotoGrid(photoId = photoId, db = db)}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ){
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "画像を追加",
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(22.dp),
            contentAlignment = Alignment.Center
        ) {
        }
    }
}

@Composable
fun PhotoGrid(photoId: Int?,db: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()
    var storages by remember { mutableStateOf<List<Strage>>(emptyList()) }
    var ia by remember { mutableIntStateOf(0) }

    LaunchedEffect(ia){
        coroutineScope.launch(Dispatchers.IO){
            db.strageDao().getAll().collect{directories ->
                withContext(Dispatchers.Main){
                    storages = emptyList()
                    for(d in directories){
                        if(photoId == d.photoId){
                            storages += d
                        }
                    }
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp)
    ) {

        items(storages) { storage ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(File(storage.uri)),
                    contentDescription = "Image $",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            ia++;
                            deleteImageFormInternalStorage(storage.uri)
                            db.strageDao().delete(storage)
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

private suspend fun saveImageToInternalStorage(context: Context, uri: Uri): String{
    return withContext(Dispatchers.IO){
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val outputFile = File(context.filesDir, fileName)

        inputStream?.use{input ->
            FileOutputStream(outputFile).use {output ->
                input.copyTo(output)
            }
        }
        outputFile.absolutePath
    }
}

private suspend fun deleteImageFormInternalStorage(filePath: String): Boolean{
    return withContext(Dispatchers.IO){
        try{
            val file = File(filePath)
            file.delete()
        }catch(e:Exception){
            e.printStackTrace()
            false
        }
    }
}