package com.vibelyze.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import com.vibelyze.R
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.lifecycle.ViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    onCreateAccountClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    viewModel: SignUpViewModel
) {
    var email by remember { mutableStateOf(viewModel.email) }
    var name by remember { mutableStateOf(viewModel.name) }
    var selectedDate by remember { mutableStateOf(
        viewModel.birthDate.takeIf { it.isNotEmpty() }?.let {
            val parts = it.split(" ")
            if(parts.size == 3) Triple(parts[0], parts[1], parts[2]) else Triple("01", "Enero", "2025")
        } ?: Triple("01", "Enero", "2025")
    ) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001F4D)) // Fondo azul marino
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it
                                viewModel.email = it },
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
                value = name,
                onValueChange = { name = it
                                viewModel.name = it },
                label = { Text("Nombre", color = Color.White) },
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

            FechaDeNacimientoSelector(
                selectedDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                    viewModel.birthDate = "${it.first} ${it.second} ${it.third}"
                },
                selectedDay = selectedDate.first,
                selectedMonth = selectedDate.second,
                selectedYear = selectedDate.third,
                onDaySelected = { newDay ->
                    selectedDate = Triple(newDay, selectedDate.second, selectedDate.third)
                },
                onMonthSelected = { newMonth ->
                    selectedDate = Triple(selectedDate.first, newMonth, selectedDate.third)
                },
                onYearSelected = { newYear ->
                    selectedDate = Triple(selectedDate.first, selectedDate.second, newYear)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    if(email.isBlank() || name.isBlank()) {
                        // Aquí podrías mostrar un mensaje de error (Snackbar o Toast)
                        return@Button
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        // Mostrar mensaje de error aquí también
                        return@Button
                    }
                    navController.navigate("confirmPasswordScreen") // Navegar a pantalla para contraseña
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Siguiente")
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                onClick = { onCreateAccountClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Crear Cuenta")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaDeNacimientoSelector(
    selectedDate: Triple<String, String, String>,
    onDateSelected: (Triple<String, String, String>) -> Unit,
    selectedDay: String,
    selectedMonth: String,
    selectedYear: String,
    onDaySelected: (String) -> Unit,
    onMonthSelected: (String) -> Unit,
    onYearSelected: (String) -> Unit
) {
    val days = (1..31).map { it.toString().padStart(2, '0') }
    val months = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    val years = (2024 downTo 1900).map { it.toString() }

    var (selectedDay, selectedMonth, selectedYear) = selectedDate


    var expandedDay by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }

    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Cyan,
        unfocusedIndicatorColor = Color.Gray,
        cursorColor = Color.White,
        focusedContainerColor = Color(0xFF1F1F1F),
        unfocusedContainerColor = Color(0xFF1F1F1F),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )


    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Fecha de nacimiento",
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Día
            ExposedDropdownMenuBox(
                expanded = expandedDay,
                onExpandedChange = { expandedDay = !expandedDay },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 4.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedDay,
                    onValueChange = {},
                    label = { Text("Día", color = Color.White) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDay) },
                    colors = textFieldColors,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedDay,
                    onDismissRequest = { expandedDay = false }
                ) {
                    days.forEach {
                        DropdownMenuItem(
                            text = { Text(it, color = Color.White) },
                            onClick = {
                                selectedDay = it
                                expandedDay = false
                                onDaySelected(it)
                                onDateSelected(Triple(selectedDay, selectedMonth, selectedYear))
                            }
                        )
                    }
                }
            }

            // Mes
            ExposedDropdownMenuBox(
                expanded = expandedMonth,
                onExpandedChange = { expandedMonth = !expandedMonth },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedMonth,
                    onValueChange = {},
                    label = { Text("Mes", color = Color.White) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                    colors = textFieldColors,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedMonth,
                    onDismissRequest = { expandedMonth = false }
                ) {
                    months.forEach {
                        DropdownMenuItem(
                            text = { Text(it, color = Color.White) },
                            onClick = {
                                selectedMonth = it
                                expandedMonth = false
                                onMonthSelected(it)
                                onDateSelected(Triple(selectedDay, selectedMonth, selectedYear))
                            }
                        )
                    }
                }
            }

            // Año
            ExposedDropdownMenuBox(
                expanded = expandedYear,
                onExpandedChange = { expandedYear = !expandedYear },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedYear.toString(),
                    onValueChange = {},
                    label = { Text("Año", color = Color.White) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                    colors = textFieldColors,
                    singleLine = true,
                    maxLines = 1,
                    isError = false,
                    modifier = Modifier
                        .width(10000.dp)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false }
                ) {
                    years.forEach {
                        DropdownMenuItem(
                            text = { Text(it.toString(), color = Color.White) },
                            onClick = {
                                selectedYear = it
                                expandedYear = false
                                onYearSelected(it)
                                onDateSelected(Triple(selectedDay, selectedMonth, selectedYear))
                            }
                        )
                    }
                }
            }
        }
    }
}

