package com.example.saeloup.View

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

class VillageoisView {
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Villageois() = Scaffold(
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
                .background(Color.Green.copy(alpha = 0.2f), CircleShape)
                .padding(16.dp)
        ) {
            // Placeholder for the image
            // Replace with an actual Image composable when you have the image resource
            Image(
                painter = painterResource(id = R.drawable.fourche),
                contentDescription = "Center Image",
                modifier = Modifier.size(200.dp).padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(90.dp))
        // Your form or any other composables go here
        // For example, a dropdown menu or a list of players
        DropdownMenuSample()
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
