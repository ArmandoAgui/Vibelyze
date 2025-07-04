package com.vibelyze.ui.screens.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vibelyze.R
import com.vibelyze.session.SessionManager
import com.vibelyze.ui.navigation.NavRoutes

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001F4D))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vibelyze",
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F1F1F),
                    unfocusedContainerColor = Color(0xFF1F1F1F),
                    focusedIndicatorColor = Color.Cyan,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F1F1F),
                    unfocusedContainerColor = Color(0xFF1F1F1F),
                    focusedIndicatorColor = Color.Cyan,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                                firestore.collection("users").document(uid)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        val isPremium = document.getBoolean("isPremium") ?: false
                                        SessionManager.isPremium = isPremium

                                        Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                                        navController.navigate(NavRoutes.Home) {
                                            popUpTo(NavRoutes.Login) { inclusive = true }
                                            launchSingleTop = true
                                        }

                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "No se pudo obtener info del usuario", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿Se ha olvidado de su contraseña?",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable {
                        if (email.isBlank()) {
                            Toast.makeText(context, "Ingrese su correo para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(context, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
                        } else {
                            auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Correo enviado para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate("signup") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Crear Cuenta")
            }
        }
    }
}
