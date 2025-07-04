package com.vibelyze.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vibelyze.ui.navigation.NavRoutes
import com.vibelyze.session.SessionManager
import com.vibelyze.ui.theme.Purple80
import com.vibelyze.ui.theme.PurpleGrey40
import com.vibelyze.ui.theme.Purple40

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var isPremium by remember { mutableStateOf(false) }

    LaunchedEffect(uid) {
        if (uid != null) {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    name = document.getString("name") ?: ""
                    email = document.getString("email") ?: ""
                    birthDate = document.getString("birthDate") ?: ""
                    isPremium = document.getBoolean("isPremium") ?: false
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Perfil", color = Purple80, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        ProfileInfoRow("Nombre", name)
        ProfileInfoRow("Correo", email)
        ProfileInfoRow("Nacimiento", birthDate)
        ProfileInfoRow("Estado", if (isPremium) "💎 Premium" else "Gratis")

        Spacer(modifier = Modifier.height(32.dp))

        if (!isPremium) {
            Button(
                onClick = {
                    firestore.collection("users").document(uid ?: "").update("isPremium", true)
                        .addOnSuccessListener {
                            isPremium = true
                            SessionManager.isPremium = true
                            Toast.makeText(context, "¡Ahora eres Premium! 💎", Toast.LENGTH_SHORT).show()
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hazte Premium")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                SessionManager.isPremium = false

                navController.navigate(NavRoutes.Login) {
                    popUpTo(0) { inclusive = true } // Borra toda la backstack
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Purple80)
        ) {
            Text("Cerrar sesión")
        }

    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, color = Purple80, fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}
