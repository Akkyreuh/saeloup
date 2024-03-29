package com.example.saeloup.View

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.saeloup.AppState
import com.example.saeloup.DropdownMenuSample
import com.example.saeloup.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database




class SorciereDeadView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SorciereDead(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val shouldNavigate = remember { mutableStateOf(false) }
//    val joueurs = remember { mutableStateOf(listOf<String>()) }
    val joueurs = remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    val boutonVisible = remember { mutableStateOf(true) }
    val showPopup = remember { mutableStateOf(false) }

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText = "Choisir sa cible"


    val partiePath = AppState.currentJoueurPath?.substringBefore("/Joueurs") ?: ""


    val joueurPath = AppState.currentJoueurPath ?: return
    Log.d("joueurPath", "joueurPath: ${joueurPath}")



    LaunchedEffect(partiePath) {
        val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
        deroulementRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deroulement.value = snapshot.getValue(String::class.java) ?: ""

                if (deroulement.value != "sorciere") {
                    shouldNavigate.value = true
                }
                Log.d(
                    "RoomView",
                    "Deroulement: ${deroulement.value}, Should Navigate: ${shouldNavigate.value}"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

        // Récupérer la liste des joueurs non-loups et vivants
        val joueursRef = Firebase.database.reference.child("$partiePath/Joueurs")
        joueursRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val joueursList = snapshot.children.mapNotNull { joueurSnapshot ->
                    val pseudo = joueurSnapshot.child("pseudo").getValue(String::class.java)
                    val role = joueurSnapshot.child("role").getValue(String::class.java)
                    val etat = joueurSnapshot.child("etat").getValue(String::class.java)
                    val id = joueurSnapshot.key // ou une autre façon d'obtenir l'ID unique

                    if (etat == "vivant" && pseudo != null && id != null) {
                        Pair(pseudo.trim(), id)
                    } else {
                        null
                    }
                }
                joueurs.value = joueursList
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })


    }

    if (shouldNavigate.value) {
        Log.d("RoomView", "humm")
        navController.navigate("modelnavigation")
        shouldNavigate.value = false
    }


    // Fonction pour trouver l'ID d'un joueur basé sur son pseudo
    fun trouveIdDuJoueur(pseudo: String): String? {
        return joueurs.value.find { it.first == pseudo }?.second
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {},
                navigationIcon = {
                    // Placeholder for the button at the top left
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
                    }
                },
                actions = {
                    // Placeholder for the button at the top right
                    IconButton(onClick = { showPopup.value = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More actions")
                    }

                    if (showPopup.value) {
                        PopupContent(onDismiss = { showPopup.value = false })
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
                    .background(Color(0xFF5D3B76), CircleShape)
                    .padding(16.dp)
            ) {
                // Placeholder for the image
                // Replace with an actual Image composable when you have the image resource
                Image(
                    painter = painterResource(id = R.drawable.potion__2_),
                    contentDescription = "Center Image",
                    modifier = Modifier
                        .size(168.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(90.dp))
            // Your form or any other composables go here
            // For example, a dropdown menu or a list of players


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        joueurs.value.forEach { (pseudo, _) -> // Utilise la déstructuration pour obtenir seulement le pseudo
                            DropdownMenuItem(
                                text = { Text(text = pseudo) }, // Affiche le pseudo
                                onClick = {
                                    selectedText = pseudo // Utilise le pseudo pour selectedText
                                    expanded = false
                                    // Toast.makeText(context, pseudo, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Send button
            Button(
                onClick = {
                    val joueurId = trouveIdDuJoueur(selectedText)
                    if (joueurId != null) {
                        val etatRef = Firebase.database.reference.child("$partiePath/Joueurs/$joueurId/etat")
                        val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")

                        etatRef.setValue("presqueMort")
                        deroulementRef.setValue("passageJour")
                    }
                }
                ,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.5f)
                    .height(72.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8D8D8))
            ) {
                    Image(
                        painter = painterResource(id = R.drawable.potion__2_),
                        contentDescription = "Vote Image",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

        }
    }


@Composable
fun PopupContentWitch(onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Galerie") },
        text = {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                // Premier élément
                BoxWithContentWitch(imageId = R.drawable.loup, text = "LE LOUP GAROU EST TRES VILAIN ET MANGE DES VILLAGEOIS")
                Spacer(modifier = Modifier.height(16.dp))

                // Deuxième élément
                BoxWithContentWitch(imageId = R.drawable.fourche, text = "LE VILLAGEOIS DOIT EMPALLER LES VILAINS LOUP GAROUS")
                Spacer(modifier = Modifier.height(16.dp))

                // Troisième élément
                BoxWithContentWitch(imageId = R.drawable.crystal, text = "LA VOYANTE REGARDE DANS SA BOULE ET DEVINE LE ROLE DES GENS")
                Spacer(modifier = Modifier.height(16.dp))

                // Quatrième élément
                BoxWithContentWitch(imageId = R.drawable.fusil, text = "LE CHASSEUR MET UNE GROSSE BALLE ENTRE LES YEUX DE CELUI QU'IL VEUT QUANT IL CREVE")
                Spacer(modifier = Modifier.height(16.dp))

                // Cinquième élément
                BoxWithContentWitch(imageId = R.drawable.potion__3_, text = "LA SORCIERE PEUT CHOISIR DE SAUVER OU DE TUER QUELQU'UN, UNE FOIS DE CHAQUE")
                Spacer(modifier = Modifier.height(16.dp))

                BoxWithContentWitch(imageId = R.drawable.bouclier__1_, text = "LE GARDE PROTEGE LES PETITS VILLAGEOIS AVEC SON GROS BOUCLIER")
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Couleur de fond du bouton
                    contentColor = Color.White // Couleur du texte et de l'icône
                )
            ) {
                Text(
                    "OK",
                    fontWeight = FontWeight.Bold, // Rendre le texte du bouton gras
                    color = Color.White // Définir explicitement la couleur du texte si nécessaire
                )
            }
        }
    )
}


@Composable
fun BoxWithContentWitch(imageId: Int, text: String) {
    // Utiliser Column au lieu de Box pour empiler verticalement
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Centrer horizontalement les éléments dans la colonne
        modifier = Modifier
            .width(200.dp) // Réduire la largeur de la colonne
            .padding(8.dp) // Ajouter un peu de padding autour de la colonne
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Image",
            modifier = Modifier
                .height(150.dp) // Réduire la hauteur de l'image
                .padding(8.dp) // Ajouter un peu de padding autour de l'image
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacer l'image du texte
        Text(
            text = text,
            textAlign = TextAlign.Center, // Centrer le texte
            modifier = Modifier.padding(horizontal = 4.dp) // Ajouter un peu de padding sur les côtés du texte
        )
    }
}


