package com.example.saeloup

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.database.*

@Composable
fun ModelNavigation(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val roleJoueur = remember { mutableStateOf("") }

    val joueurPath = AppState.currentJoueurPath ?: return

    LaunchedEffect(joueurPath) {
        val joueurRef = Firebase.database.reference.child(joueurPath)
        joueurRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roleJoueur.value = snapshot.child("role").getValue(String::class.java) ?: ""
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
                navigateBasedOnDeroulementEtRole(deroulement.value, roleJoueur.value, navController)
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })
    }

    // Contenu de ModelNavigation si nécessaire
}

fun navigateBasedOnDeroulementEtRole(deroulement: String, roleJoueur: String, navController: NavController) {
    when {
        deroulement == "nuit" || deroulement == "jour" -> navController.navigate("${deroulement}View")
        deroulement == roleJoueur -> navController.navigate("${deroulement}View")
        else -> {
            // Gérer les cas inattendus ou la valeur par défaut
        }
    }
}
