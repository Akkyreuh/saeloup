package com.example.saeloup.View

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.saeloup.DropdownMenuSample
import com.example.saeloup.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.saeloup.AppState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.database.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.navigation.compose.composable
import com.example.saeloup.ModelNavigation
import com.google.firebase.database.*


class RoomView {
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Room(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val joueurs = remember { mutableStateOf(listOf<String>()) }
    val shouldNavigate = remember { mutableStateOf(false) }

    val partiePath = AppState.currentJoueurPath?.substringBefore("/Joueurs") ?: ""

    LaunchedEffect(partiePath) {
        val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
        deroulementRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deroulement.value = snapshot.getValue(String::class.java) ?: ""

                if (deroulement.value == "start") {
                    shouldNavigate.value = true
                }
                Log.d("RoomView", "Deroulement: ${deroulement.value}, Should Navigate: ${shouldNavigate.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

        // Récupérer la liste des joueurs
        val joueursRef = Firebase.database.reference.child("$partiePath/Joueurs")
        joueursRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val joueursList = snapshot.children.map { it.child("pseudo").getValue(String::class.java) ?: "Inconnu" }
                joueurs.value = joueursList
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
//        Log.d("RoomView", "Deroulement: ${deroulement.value}, Should Navigate: ${shouldNavigate.value}")

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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Deroulement: ${deroulement.value}") // Affichage pour débogage
            Text("Should Navigate: ${shouldNavigate.value}") // Affichage pour débogage

            Text("Bienvenue dans la salle d'attente")

            // Afficher la liste des joueurs
            LazyColumn {
                items(joueurs.value) { joueur ->
                    Text(joueur)
                }
            }
        }
    }
}