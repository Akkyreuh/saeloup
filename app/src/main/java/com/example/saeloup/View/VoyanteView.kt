package com.example.saeloup.View

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.saeloup.AppState
import com.example.saeloup.DropdownMenuSample
import com.example.saeloup.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class VoyanteView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Voyante(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val shouldNavigate = remember { mutableStateOf(false) }
//    val joueurs = remember { mutableStateOf(listOf<String>()) }
    val joueurs = remember { mutableStateOf(listOf<Pair<String, String>>()) }

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

                if (deroulement.value != "voyante") {
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

        // Récupérer la liste des joueurs  vivants
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .background(Color(0xFFFD87DF8), CircleShape)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.crystal),
                    contentDescription = "Center Image",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)

                )
            }
            Spacer(modifier = Modifier.height(32.dp))
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
            Button(
                onClick = {
                    val joueurId = trouveIdDuJoueur(selectedText)
                    if (joueurId != null) {
                        showPopup.value = true
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.5f)
                    .height(72.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8D8D8))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dessin_anime_yeux_heureux),
                    contentDescription = "Vote Image",
                    modifier = Modifier.size(50.dp)
                )
            }
            if (showPopup.value) {
                PopupContents(onDismiss = { showPopup.value = false }, joueurId = trouveIdDuJoueur(selectedText) ?: "", partiePath = partiePath)
            }
        }
    }
}

@Composable
fun PopupContents(onDismiss: () -> Unit, joueurId: String,  partiePath: String) {
    var roleDuJoueur by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(joueurId) {
        val joueurRef = Firebase.database.reference.child("$partiePath/Joueurs/$joueurId")
        joueurRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roleDuJoueur = snapshot.child("role").getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
    }

    AlertDialog(
        onDismissRequest = { },
        // autres propriétés de l'AlertDialog...
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Centre les éléments dans la colonne
                modifier = Modifier.fillMaxWidth() // Assurez-vous que la colonne prend toute la largeur disponible
            ) {
                Text("Rôle du joueur sélectionné :")
                Spacer(modifier = Modifier.height(10.dp))
                if (roleDuJoueur != null) {
                    Text(
                        text = roleDuJoueur!!,
                        fontWeight = FontWeight.Bold, // Met le texte en gras
                        textAlign = TextAlign.Center, // Centre le texte
                        modifier = Modifier
                            .fillMaxWidth() // Assurez-vous que le Text prend toute la largeur disponible
                            .wrapContentSize(Alignment.Center) // Centre le contenu dans le Text
                    )
                } else {
                    Text("Chargement...")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
                    deroulementRef.setValue("loup").addOnSuccessListener {
                        onDismiss()
                    }.addOnFailureListener { e ->
                        // Gérer l'échec de la mise à jour
                        Log.e("AlertDialog", "Erreur lors de la mise à jour de deroulement", e)
                    }
                },
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



