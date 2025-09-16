package com.example.studypractice_businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.studypractice_businesscard.ui.theme.StudyPractice_BusinessCardTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyPractice_BusinessCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFD2E8D4) // 배경색 설정
                ) {
                    BusinessCard("wnghks", "Android Developer")
                }
            }
        }
    }
}

@Composable
fun BusinessCard(fullname: String, title: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val image = painterResource(R.drawable.android_logo)
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .background(Color(0xFF073042))
                    .size(120.dp)
            )
            Text(
                text = fullname,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 16.dp,bottom = 8.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color(0xFF006D3B),
                fontWeight = FontWeight.Bold
            )
        }
        ContactInfo()
    }
}
@Composable
fun ContactInfo(phone_number: String = "+00 00 000 000",social_handle: String = "@Username",email: String = "email@gmail.com", modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(bottom = 48.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        ContactRow(
            icon = Icons.Rounded.Call,
            text = phone_number
        )
        ContactRow(
            icon = Icons.Rounded.Share,
            text = social_handle
        )
        ContactRow(
            icon = Icons.Rounded.Email,
            text = email
        )
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    // 아이콘과 텍스트를 가로로 배치하기 위한 Row
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically // 아이콘과 텍스트의 세로 중앙을 맞춤
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF006D3B)
        )
        Spacer(Modifier.width(24.dp)) // 아이콘과 텍스트 사이의 간격
        Text(
            text = text,
            color = Color.Black
        )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessCardPreview() {
    StudyPractice_BusinessCardTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFD2E8D4)
        ) {
            BusinessCard("wnghks", "Android Developer")
        }
    }
}