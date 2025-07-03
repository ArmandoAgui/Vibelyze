package com.vibelyze.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

//CLases que sirve para almacenar los datos de la creacion de la cuenta cuando se esta entre pantallas
class SignUpViewModel : ViewModel() {
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var birthDate by mutableStateOf("")
}
