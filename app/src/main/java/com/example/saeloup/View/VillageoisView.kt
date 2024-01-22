package com.example.saeloup.View

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.TextFieldValue
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

class VillageoisView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Villageois(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val shouldNavigate = remember { mutableStateOf(false) }
    val joueurs = remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val boutonVisible = remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    var selectedText = remember { mutableStateOf("Choisir un joueur") }

    val partiePath = AppState.currentJoueurPath?.substringBefore("/Joueurs") ?: ""

    LaunchedEffect(partiePath) {
        val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
        deroulementRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deroulement.value = snapshot.getValue(String::class.java) ?: ""

                if (deroulement.value != "villageois") {
                    shouldNavigate.value = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

        val joueursRef = Firebase.database.reference.child("$partiePath/Joueurs")
        joueursRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val joueursList = snapshot.children.mapNotNull { joueurSnapshot ->
                    val pseudo = joueurSnapshot.child("pseudo").getValue(String::class.java)
                    val etat = joueurSnapshot.child("etat").getValue(String::class.java)
                    val id = joueurSnapshot.key

                    if (etat == "vivant" && pseudo != null && id != null) Pair(pseudo.trim(), id) else null
                }
                joueurs.value = joueursList
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
                    .background(Color.Green.copy(alpha = 0.2f), CircleShape)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fourche),
                    contentDescription = "Center Image",
                    modifier = Modifier.size(200.dp).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Liste déroulante des joueurs
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
                        value = selectedText.value,
                        onValueChange = { selectedText.value = it },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        joueurs.value.forEach { (pseudo, _) ->
                            DropdownMenuItem(
                                text = { Text(text = pseudo) },
                                onClick = {
                                    selectedText.value = pseudo
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bouton pour voter
            if (boutonVisible.value) {
                Button(
                    onClick = {
                        val joueurId = trouveIdDuJoueur(selectedText.value)
                        if (joueurId != null) {
                            val joueurRef = Firebase.database.reference.child("$partiePath/Joueurs/$joueurId/vote")
                            joueurRef.runTransaction(object : Transaction.Handler {
                                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                    val currentVote = mutableData.getValue(Int::class.java) ?: 0
                                    mutableData.value = currentVote + 1
                                    return Transaction.success(mutableData)
                                }

                                override fun onComplete(databaseError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                                    // Traiter la fin de la transaction ici
                                }
                            })
                        }
                        boutonVisible.value = false
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.5f)
                        .height(72.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.urne_electorale),
                        contentDescription = "Vote Image",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}