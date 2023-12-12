package com.example.saeloup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScreen() {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {},
                navigationIcon = {
                    // Placeholder for the button at the top left
                    IconButton(onClick = { /* TODO: Handle left icon click */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    // Placeholder for the button at the top right
                    IconButton(onClick = { /* TODO: Handle right icon click */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More actions")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle for the image at the center of the screen
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.Green.copy(alpha = 0.2f), CircleShape)
                    .padding(16.dp)
            ) {
                // Placeholder for the image
                // Replace with an actual Image composable when you have the image resource
                Image(
                    painter = painterResource(id = R.drawable.fourche),
                    contentDescription = "Center Image",
                    modifier = Modifier.size(200.dp).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Your form or any other composables go here
            // For example, a dropdown menu or a list of players
            DropdownMenuSample()
            Spacer(modifier = Modifier.height(24.dp))
            // Send button
            Button(
                onClick = { /* TODO: Handle send button click */ },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Vote")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSample() {
    var expanded by remember { mutableStateOf(false) }
    val items = List(10) { "Joueur $it" }
    var selectedIndex by remember { mutableStateOf(0) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = items[selectedIndex],
            onValueChange = { },
            label = { Text("Sélectionnez un joueur") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    }
                )
            }
        }
    }
}


/**
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
**/
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