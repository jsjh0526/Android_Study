package com.example.studypractice_businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.studypractice_businesscard.ui.theme.StudyPractice_BusinessCardTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyPractice_BusinessCardTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        fullname = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
            }
        }
    }
}

@Composable
fun Greeting(fullname: String, title: String, modifier: Modifier = Modifier) {
    Box (modifier) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            val image = painterResource(R.drawable.android_logo)
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(1.dp)
                    .size(30.dp)

            )
            Text(
                text = fullname,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.size(70.dp,16.dp)
            )
            Text(
                text = title,
                fontSize = 5.sp,
                color = Color(0xFF3ddc84),
                modifier = modifier,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun call(PhoneNum: String, Id: String, Email: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = PhoneNum,
            fontSize = 10.sp,
            modifier = modifier
        )
        Text(
            text = Id,
            fontSize = 5.sp,

            modifier = modifier
        )
        Text(
            text = Email,
            fontSize = 5.sp,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudyPractice_BusinessCardTheme {
        Greeting("Juhwan Lee","Android Developer Extraodinaire")
        //call("+00 00 000 000","@Username","email@gmail.com")
    }
}