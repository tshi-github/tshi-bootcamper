package com.example.basicofkotlin.Display

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.basicokotlin.R

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: flag
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(R.drawable.nex),
                contentDescription = "Contact profile picture",
                modifier = Modifier.fillMaxWidth()
            )

            if(viewModel.showButton)

            Button(
                modifier = Modifier
                    .padding(end = 36.dp, bottom = 24.dp),
                onClick = {navController.navigate("main")}
            ) {
                Text("Continue",
                    fontSize = 24.sp
                )
            }
        }
    }
}

class flag : ViewModel(){
    val showButton = true
}