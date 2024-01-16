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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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




class LoupView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Loup(navController: NavController) {
    val deroulement = remember { mutableStateOf("") }
    val shouldNavigate = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coffeeDrinks = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }


    val partiePath = AppState.currentJoueurPath?.substringBefore("/Joueurs") ?: ""

    LaunchedEffect(partiePath) {
        val deroulementRef = Firebase.database.reference.child("$partiePath/deroulement")
        deroulementRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deroulement.value = snapshot.getValue(String::class.java) ?: ""

                if (deroulement.value != "loup") {
                    shouldNavigate.value = true
                }
                Log.d("RoomView", "Deroulement: ${deroulement.value}, Should Navigate: ${shouldNavigate.value}")
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
                // Circle for the image at the center of the screen
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color(0xFFF46363), CircleShape)
                        .padding(16.dp)
                ) {
                    // Placeholder for the image
                    // Replace with an actual Image composable when you have the image resource
                    Image(
                        painter = painterResource(id = R.drawable.loup),
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
                            coffeeDrinks.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedText = item
                                        expanded = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                // Send button
                Button(
                    onClick = {},
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Vote")
                }
            }

    }
}
