package com.example.saeloup.View

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
        Spacer(modifier = Modifier.height(90.dp))
        Text(
            text = "Joueur 1 est visé",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        // Deux boutons sur la même ligne
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Button(
                onClick = {},
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
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {navController.navigate("SorciereDeadView")},
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
        // Un bouton sur la ligne en dessous
        Button(
            onClick = {},
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
    }
}