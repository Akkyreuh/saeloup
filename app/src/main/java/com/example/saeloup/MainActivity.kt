package com.example.saeloup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.saeloup.ui.theme.SaeloupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaeloupTheme {
                // Setup NavController
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(navController)
                    }
                    composable("newScreen") {
                        NewScreen()
                    }
                    composable("Card") {
                        NewScreen()
                    }
                }
            }
        }
    }
}
@Composable
fun MainScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting("ROARRR")
            Spacer(modifier = Modifier.height(16.dp))
            SimpleFilledTextFieldSample("PSEUDO :")
            SimpleFilledTextFieldSample("CODE PIN")
            Spacer(modifier = Modifier.weight(1f))
            ValiderButton(onClick = { navController.navigate("newScreen") })
        }
    }
}

@Composable
fun NewScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Greeting("ROARRR")
        Spacer(modifier = Modifier.height(16.dp))

        // Afficher les joueurs de 1 à 10
        for (i in 1..10) {
            Text("Joueur $i")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f)) // Utiliser l'espace restant
        ValiderButton(onClick = { /* Action pour le bouton Valider */ })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier,
    )
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

@Composable
fun ValiderButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        content = { Text(text = "Valider") },
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SaeloupTheme {
        Greeting("ROARRR", modifier = Modifier.padding(top = 32.dp))
    }
}