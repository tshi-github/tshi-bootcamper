package com.example.basicofkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.basicofkotlin.Display.Greetings.Greetings
import com.example.basicofkotlin.Display.HeadFooter.Header
import com.example.basicofkotlin.Display.OnboardingScreen
import com.example.basicofkotlin.Display.PhotoFile
import com.example.basicofkotlin.Display.flag
import com.example.basicofkotlin.Room.AppDatabase
import com.example.basicofkotlin.Room.MIGRATION_2_3
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
            ).addMigrations(MIGRATION_2_3).build()
            val viewModel: flag = viewModel()

            BasicOfKotlinTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Surface(color = MaterialTheme.colorScheme.background){
                        Header()
                    }

                    Surface(color = MaterialTheme.colorScheme.background) {
                        MyApp(
                            db = db,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(db: AppDatabase, viewModel: flag, navController: NavHostController){

    NavHost(navController = navController, startDestination = "login"){
        composable("login") { OnboardingScreen(navController = navController, viewModel = viewModel) }
        composable("main"){ Greetings(navController = navController, db = db) }
        composable("file/{photoId}"){backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId")?.toIntOrNull()
            PhotoFile(photoId = photoId, db = db)}
    }
}
