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
import com.example.saeloup.View.LoupView
import com.example.saeloup.View.Room
import com.example.saeloup.View.Sorciere
import com.example.saeloup.View.SorciereView
import com.example.saeloup.View.Chasseur
import com.example.saeloup.View.ChasseurView
import com.example.saeloup.View.Garde
import com.example.saeloup.View.GardeView
import com.example.saeloup.View.Cupidon
import com.example.saeloup.View.CupidonView
import com.example.saeloup.View.Villageois
import com.example.saeloup.View.VillageoisView
import com.example.saeloup.View.Voyante
import com.example.saeloup.View.VoyanteView
import com.example.saeloup.ui.theme.SaeloupTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

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
//                        Sorciere(navController)
                    }
                    composable("newScreen") {
                        Room(navController)
                    }
                    composable("modelnavigation") {
                        ModelNavigation(navController)
                    }
                    composable("loupView") {
                        Loup(navController)
                    }
                    composable("villageoisView") {
                        Villageois(navController)
                    }
                    composable("voyanteView") {
                        Voyante(navController)
                    }
                    composable("chasseurView") {
                        Chasseur(navController)
                    }
                    composable("sorciereView") {
                        Sorciere(navController)
                    }
                    composable("gardeView") {
                        Garde(navController)
                    }
                    composable("cupidonView") {
                        Cupidon(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val pseudo = remember { mutableStateOf("") }
    val pin = remember { mutableStateOf("") }

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


            SimpleFilledTextFieldSample(
                text = pin.value,
                onValueChange = { newValue -> pin.value = newValue },
                label = "CODE PIN"
            )

            SimpleFilledTextFieldSample(
                text = pseudo.value,
                onValueChange = { newValue -> pseudo.value = newValue },
                label = "PSEUDO :"
            )



            Spacer(modifier = Modifier.weight(1f))

            // Bouton de validation
            ValiderButton(onClick = { verifierEtAjouterJoueur(pin.value, pseudo.value, navController) })
        }
    }
}

object AppState {
    var currentJoueurPath: String? = null
}
//fun verifierEtAjouterJoueur(pin: String, pseudo: String, navController: NavController) {
//    val databaseReference = Firebase.database.reference.child("Partie$pin")
//
//    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            if (snapshot.exists()) {
//                // Ajouter un joueur
//                val joueur = mapOf(
//                    "etat" to "vivant",
//                    "id" to snapshot.child("Joueurs").childrenCount + 1,
//                    "role" to "loup", // ou une autre logique pour déterminer le rôle
//                    "pseudo" to pseudo,
//                    "vote" to 0
//                )
//                databaseReference.child("Joueurs").child("Joueur${snapshot.child("Joueurs").childrenCount + 1}").setValue(joueur)
//                val joueurPath = "Partie$pin/Joueurs/Joueur${snapshot.child("Joueurs").childrenCount}"
//                AppState.currentJoueurPath = joueurPath
//
//
//                // Naviguer vers le nouvel écran
//                navController.navigate("newScreen")
//            } else {
//                // Gérer le cas où le code PIN n'existe pas
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            // Gérer les erreurs
//        }
//    })
//}


fun verifierEtAjouterJoueur(pin: String, pseudo: String, navController: NavController) {
    val databaseReference = Firebase.database.reference.child("Partie$pin")

    // Structure pour gérer les rôles
    val rolesMax = mapOf(
        "loup" to 2,
        "villageois" to 2, // Cette valeur peut être ajustée si nécessaire
        "voyante" to 1,
        "sorciere" to 1,
        "chasseur" to 1,
        "garde" to 1,
        "cupidon" to 1
    )

    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                // Compter les rôles actuels
                val rolesCount = mutableMapOf<String, Int>()
                snapshot.child("Joueurs").children.forEach { joueurSnapshot ->
                    val role = joueurSnapshot.child("role").getValue(String::class.java) ?: return
                    rolesCount[role] = rolesCount.getOrDefault(role, 0) + 1
                }

                // Filtrer les rôles disponibles
                val rolesDisponibles = rolesMax.filter { (role, max) ->
                    rolesCount.getOrDefault(role, 0) < max
                }.keys.toList()

                // Sélectionner un rôle aléatoire ou le rôle de villageois
                val roleAttribue = if (rolesDisponibles.isNotEmpty()) {
                    rolesDisponibles.random()
                } else {
                    "villageois" // Attribuer le rôle de villageois si aucun autre rôle n'est disponible
                }

                // Ajouter un joueur avec le rôle attribué
                val joueur = mapOf(
                    "etat" to "vivant",
                    "id" to snapshot.child("Joueurs").childrenCount + 1,
                    "role" to roleAttribue,
                    "pseudo" to pseudo,
                    "vote" to 0
                )
                databaseReference.child("Joueurs").child("Joueur${snapshot.child("Joueurs").childrenCount + 1}").setValue(joueur)
                val joueurPath = "Partie$pin/Joueurs/Joueur${snapshot.child("Joueurs").childrenCount + 1}"
                AppState.currentJoueurPath = joueurPath

                // Naviguer vers le nouvel écran
                navController.navigate("newScreen")
            } else {
                // Gérer le cas où le code PIN n'existe pas
                // Ajoutez ici votre logique pour gérer ce cas
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Gérer les erreurs
            // Ajoutez ici votre logique pour gérer les erreurs de base de données
        }
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilledTextFieldSample(text: String, onValueChange: (String) -> Unit, label: String) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) }
        // Vous pouvez ajouter d'autres paramètres ici, comme des modificateurs
    )
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