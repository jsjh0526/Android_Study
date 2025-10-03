package com.example.study_simpleclickapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study_simpleclickapp.ui.theme.Study_simpleClickAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Study_simpleClickAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    lemonaid(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun lamonaid(name: String, modifier: Modifier = Modifier) {

}


@Composable
fun imagine_text(name: String, modifier: Modifier = Modifier) {
    var lowtext = stringResource(R.string.step1)
    var image = painterResource(R.drawable.lemon_tree)

    Column(){
        Button(
            onClick = { /*TODO*/ },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
            ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = modifier
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = lowtext,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun lemonaidPreview() {
    Study_simpleClickAppTheme {
        lemonaid()
    }
}