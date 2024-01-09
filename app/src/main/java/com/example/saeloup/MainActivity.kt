package com.example.saeloup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.saeloup.View.Loup
import com.example.saeloup.View.Room
import com.example.saeloup.View.Villageois
import com.example.saeloup.View.Voyante
import com.example.saeloup.ui.theme.SaeloupTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaeloupTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(navController)
                    }
                    composable("newScreen") {
                        Voyante(navController = navController)
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
        content = { Text(text = "   Valider") },
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