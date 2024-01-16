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

class GardeView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Garde(navController: NavController) = Scaffold(
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
                painter = painterResource(id = R.drawable.bouclier__1_),
                contentDescription = "Center Image",
                modifier = Modifier
                    .size(168.dp)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(90.dp))
        // Your form or any other composables go here
        // For example, a dropdown menu or a list of players
        DropdownMenuSample()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            IconButton(onClick = { var expanded = true }) {
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

