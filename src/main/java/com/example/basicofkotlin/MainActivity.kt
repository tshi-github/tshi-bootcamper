package com.example.basicofkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.basicofkotlin.Display.Greetings.Greetings
import com.example.basicofkotlin.Display.OnboardingScreen
import com.example.basicofkotlin.Display.PhotoFile
import com.example.basicofkotlin.Display.flag
import com.example.basicofkotlin.Room.AppDatabase
import com.example.basicofkotlin.Room.MIGRATION_2_3
import com.example.basicofkotlin.Room.PhotoDao
import com.example.basicofkotlin.Room.StrageDao
import com.example.basicofkotlin.ui.theme.BasicOfKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).addMigrations(MIGRATION_2_3)
             .build()
            val photoDao = db.photoDao()
            val strageDao = db.strageDao()
            val viewModel: flag = viewModel()

            BasicOfKotlinTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Surface(color = MaterialTheme.colorScheme.background){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = "車両リスト",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier
                                    .padding(36.dp)
                            )
                        }
                    }

                    Surface(color = MaterialTheme.colorScheme.background) {
                        MyApp(
                            modifier = Modifier.fillMaxSize(),
                            photoDao = photoDao,
                            strageDao = strageDao,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, photoDao: PhotoDao, strageDao: StrageDao, viewModel: flag){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login"){
        composable("login") { OnboardingScreen(navController = navController, viewModel = viewModel) }
        composable("main"){ Greetings(navController = navController, photoDao = photoDao, strageDao = strageDao) }
        composable("file/{photoId}"){backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId")?.toIntOrNull()
            PhotoFile(navController = navController, photoDao = photoDao, photoId = photoId, strageDao = strageDao)}
    }
}
