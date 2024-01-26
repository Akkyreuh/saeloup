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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.saeloup.AppState
import com.example.saeloup.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SorciereView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Sorciere(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val shouldNavigate = remember { mutableStateOf(false) }
    val joueurs = remember { mutableStateOf(listOf<Pair<String, String>>()) }


    val showPopup = remember { mutableStateOf(false) }

    val partiePath = AppState.currentJoueurPath?.substringBefore("/Joueurs") ?: ""


    val joueurPath = AppState.currentJoueurPath ?: return
    Log.d("joueurPath", "joueurPath: ${joueurPath}")

    val joueurPresqueMort = remember { mutableStateOf("") }
    val joueurPresqueMortId = remember { mutableStateOf<Int?>(null) }

    val popoHealUse = remember { mutableStateOf(false) }
    val popoDeathUse = remember { mutableStateOf(false) }
    val joueurRef = Firebase.database.reference.child(joueurPath)


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

        val joueursRef = Firebase.database.reference.child("$partiePath/Joueurs")
        joueursRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Parcourir tous les joueurs pour trouver celui avec l'état "presqueMort"
                snapshot.children.forEach { joueurSnapshot ->
                    val etat = joueurSnapshot.child("etat").getValue(String::class.java)
                    val pseudo = joueurSnapshot.child("pseudo").getValue(String::class.java)
                    val id = joueurSnapshot.child("id").getValue(Int::class.java)

                    if (etat == "presqueMort" && pseudo != null) {
                        joueurPresqueMort.value = pseudo.trim()
                        joueurPresqueMortId.value = id
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })


        joueurRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Mettre à jour popoHealUse et popoDeathUse à true si les champs existent
                if (snapshot.hasChild("popoHeal")) {
                    popoHealUse.value = true
                }
                if (snapshot.hasChild("popoDeath")) {
                    popoDeathUse.value = true
                }

                Log.d("FirebaseData", "PopoHealUse: ${popoHealUse.value}, PopoDeathUse: ${popoDeathUse.value}")
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .background(Color(0xFFFAE2EEA), CircleShape)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chapeau_de_sorciere),
                    contentDescription = "Center Image",
                    modifier = Modifier
                        .size(168.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = if (joueurPresqueMort.value.isNotEmpty()) "${joueurPresqueMort.value} est visé" else "Aucun joueur visé",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                if (popoHealUse.value == false){
                Button(
                    onClick = {
                        val popoHealRef = Firebase.database.reference.child("$joueurPath/popoHeal")
                        popoHealRef.setValue(true)
                        val joueurId = joueurPresqueMortId.value
                        Log.d("joueurId", "$joueurId")
                        val etatRef =
                            Firebase.database.reference.child("$partiePath/Joueurs/Joueur$joueurId/etat")
                        val deroulementRef =
                            Firebase.database.reference.child("$partiePath/deroulement")
                        etatRef.setValue("vivant")
                        deroulementRef.setValue("passageJour")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8D8D8))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.potion__3_),
                        contentDescription = "Vote Image",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
                Spacer(modifier = Modifier.width(16.dp))
                if (popoDeathUse.value == false){
                    Button(
                        onClick = {
                            val popoDeathRef = Firebase.database.reference.child("$joueurPath/popoDeath")
                            popoDeathRef.setValue(true).addOnSuccessListener {
                                navController.navigate("SorciereDeadView")
                            }
                                  },
                        modifier = Modifier
                            .weight(1f)
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
            Button(
                onClick = {
                    val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
                    deroulementRef.setValue("passageJour")
                          },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.5f)
                    .height(72.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8D8D8))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.croix),
                    contentDescription = "Vote Image",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

