package com.example.saeloup.View

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.saeloup.DropdownMenuSample
import com.example.saeloup.R

class VoyanteView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Voyante(navController: NavController) = Scaffold(
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
                modifier = Modifier.size(200.dp).padding(8.dp)

            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        DropdownMenuSample()
        Spacer(modifier = Modifier.height(24.dp))
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