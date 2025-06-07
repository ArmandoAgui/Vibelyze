package com.vibelyze.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    val backgroundColor = Color(0xFF03174C)
    val cardColor = Color(0xFF1F2E5E)
    val textColor = Color(0xFFE6E7F2)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mi Perfil",
                color = textColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProfileCard("Nombre", "Armando Aguilar", cardColor, textColor)
            ProfileCard("Correo", "armando@example.com", cardColor, textColor)
            ProfileCard("Preferencias", "Chill, Rock, Electrónica", cardColor, textColor)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* TODO: Ir a pantalla de pago */ },
                colors = ButtonDefaults.buttonColors(containerColor = textColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Hazte Premium",
                    color = backgroundColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /* TODO: Cerrar sesión */ },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", color = Color.Red)
            }
        }
    }
}

@Composable
fun ProfileCard(title: String, value: String, background: Color, text: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = text.copy(alpha = 0.7f), fontSize = 14.sp)
            Text(text = value, color = text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
