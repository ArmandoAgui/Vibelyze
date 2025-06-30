package com.vibelyze.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibelyze.R

@Composable
fun TopBarMenu(
    onListenedClick: () -> Unit,
    onSavedClick: () -> Unit,
    onExitClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1B1F3B))

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            TopBarItem(
                icon = painterResource(id = R.drawable.ic_listened),
                label = "Escuchados",
                onClick = onListenedClick
            )
            TopBarItem(
                icon = painterResource(id = R.drawable.ic_saved),
                label = "Guardados",
                onClick = onSavedClick
            )
            TopBarItem(
                icon = painterResource(id = R.drawable.ic_exit),
                label = "Salir",
                onClick = onExitClick
            )
        }
    }
}

@Composable
fun TopBarItem(
   icon: Painter,
   label: String,
   onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Image(
            painter = icon,
            contentDescription = label,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.Cyan,
            fontSize = 14.sp
        )
    }
}
