package com.example.saeloup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.saeloup.ui.theme.SaeloupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaeloupTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Greeting("ROARRR", modifier = Modifier.padding(top = 32.dp))
                        SimpleFilledTextFieldSample("PSEUDO :")
                        SimpleFilledTextFieldSample("CODE PIN")
                        ValiderButton()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier,
        style = MaterialTheme.typography.h4.copy(fontSize = 30.sp)
    )
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilledTextFieldSample(label: String) {
    var textState by remember { mutableStateOf("") }

    TextField(
        value = textState,
        onValueChange = { textState = it },
        label = { Text(label) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValiderButton() {
    Button(
        onClick = { /* Traitement à effectuer lors du clic sur le bouton Valider */ },
        content = { Text(text = "Valider") },
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SaeloupTheme {
        Greeting("ROARRR", modifier = Modifier.padding(top = 32.dp))
    }
}