package com.vibelyze.ui.screens.auth

import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import com.vibelyze.R
import androidx.compose.material3.*


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun ConfirmPasswordScreen(
    navController: NavController,
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    viewModel: SignUpViewModel
) {
    val context = LocalContext.current

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    fun createAccount() {
        errorMessage = null

        if (password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Por favor ingresa ambas contraseñas"
            return
        }

        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(viewModel.email).matches()) {
            errorMessage = "Email no válido"
            return
        }

        if (viewModel.name.isBlank()) {
            errorMessage = "Nombre no válido"
            return
        }

        if (viewModel.birthDate.isBlank()) {
            errorMessage = "Fecha de nacimiento no válida"
            return
        }

        isLoading = true

        auth.createUserWithEmailAndPassword(viewModel.email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = mapOf(
                        "name" to viewModel.name,
                        "birthDate" to viewModel.birthDate,
                        "email" to viewModel.email
                    )

                    firestore.collection("users").document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            // ✅ Este bloque se ejecuta después de guardar en Firestore
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                                navController.navigate("homeScreen") {
                                    popUpTo("signup") { inclusive = true }
                                    launchSingleTop = true
                                }


                                navController.navigate("homeScreen") {
                                    popUpTo("signup") { inclusive = true }
                                    launchSingleTop = true
                                }

                            }
                        }
                        .addOnFailureListener { e ->
                            errorMessage = "Error guardando datos: ${e.message}"
                        }

                } else {
                    errorMessage = task.exception?.message ?: "Error al crear usuario"
                }
            }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001F4D))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.logo),
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
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
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña", color = Color.White) },
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { createAccount() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(if (isLoading) "Creando..." else "Crear Cuenta")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Se ha olvidado de su contraseña?",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { onForgotPasswordClick() }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate("signup") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Volver")
            }
        }
    }
}
