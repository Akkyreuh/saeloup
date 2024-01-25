package com.example.saeloup

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.database.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ModelNavigation(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val roleJoueur = remember { mutableStateOf("") }
    val etatJoueur = remember { mutableStateOf("") }

    val joueurPath = AppState.currentJoueurPath ?: return
    Log.d("joueurPath", "joueurPath: ${joueurPath}")

    LaunchedEffect(joueurPath) {
        val joueurRef = Firebase.database.reference.child(joueurPath)
        joueurRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roleJoueur.value = snapshot.child("role").getValue(String::class.java) ?: ""
                etatJoueur.value = snapshot.child("etat").getValue(String::class.java) ?: ""
                Log.d("role", "Role: ${roleJoueur.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

        val partiePath = joueurPath.substringBefore("/Joueurs")
        val partieRef = Firebase.database.reference.child(partiePath)
        partieRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deroulement.value = snapshot.child("deroulement").getValue(String::class.java) ?: ""
                Log.d("ModelNav", "Deroulement: ${deroulement.value}")
                navigateBasedOnDeroulementEtRole(deroulement.value, roleJoueur.value, etatJoueur.value, navController)
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
    }

    // Contenu de ModelNavigation si nécessaire

    Scaffold() {
        // Arrière-plan gris clair pour le contenu
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray), // Utilisez la couleur de votre choix
            contentAlignment = Alignment.Center // Aligner l'image au centre
        ) {
            // Image centrée
            Image(
                painter = painterResource(id = R.drawable.lune),
                contentDescription = "Description de l'image",
                modifier = Modifier.size(110.dp) // Ajustez la taille selon vos besoins
            )
        }
    }
}

fun navigateBasedOnDeroulementEtRole(deroulement: String, roleJoueur: String, etat: String, navController: NavController) {
    when {
//        deroulement == "nuit" || deroulement == "villageois" -> navController.navigate("${deroulement}View")
        etat == "vivant" && deroulement == "villageois" -> navController.navigate("${deroulement}View")
        etat == "vivant" && deroulement == roleJoueur -> navController.navigate("${deroulement}View")
        else -> {
            // Gérer les cas inattendus ou la valeur par défaut
        }
    }
}
