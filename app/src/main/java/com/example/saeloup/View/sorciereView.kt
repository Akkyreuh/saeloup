package com.example.saeloup.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.saeloup.DropdownMenuSample
import com.example.saeloup.R

class SorciereView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Sorciere(navController: NavController) = Scaffold(
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
                painter = painterResource(id = R.drawable.potion__2_),
                contentDescription = "Center Image",
                modifier = Modifier
                    .size(168.dp)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(90.dp))
        Text(
            text = "Joueur 1 est visé",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // Boutons "Sauver", "Tuer", et "Ne rien faire"
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Button(
                        onClick = {},
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Sauver")
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Tuer")
                    }
                }
                Button(
                    onClick = {},
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Ne rien faire")
                }
            }
        }
    }
}