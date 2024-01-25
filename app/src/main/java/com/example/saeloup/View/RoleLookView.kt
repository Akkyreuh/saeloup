package com.example.saeloup.View

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.saeloup.AppState
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RoleLook(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val shouldNavigate = remember { mutableStateOf(false) }

    val partiePath = AppState.currentJoueurPath?.substringBefore("/Joueurs") ?: ""

    val roleJoueur = remember { mutableStateOf("") }

    val joueurPath = AppState.currentJoueurPath ?: return

    LaunchedEffect(partiePath) {
        val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
        deroulementRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deroulement.value = snapshot.getValue(String::class.java) ?: ""

                if (deroulement.value == "go") {
                    shouldNavigate.value = true
                }
                Log.d("RoomView", "Deroulement: ${deroulement.value}, Should Navigate: ${shouldNavigate.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

        val joueurRef = Firebase.database.reference.child(joueurPath)
        joueurRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roleJoueur.value = snapshot.child("role").getValue(String::class.java) ?: ""
                Log.d("role", "Role: ${roleJoueur.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

    }

    if (shouldNavigate.value) {
        navController.navigate("modelnavigation")
        shouldNavigate.value = false
    }

    // UI de la page Room
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Salle d'Attente") },
                navigationIcon = {
                    IconButton(onClick = { /* Gérer le retour */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Actions supplémentaires */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Plus d'actions")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), // it représente le PaddingValues fourni par Scaffold
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texte pour afficher le rôle du joueur
            Text(
                text = roleJoueur.value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}