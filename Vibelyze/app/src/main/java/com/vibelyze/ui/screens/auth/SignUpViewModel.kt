package com.vibelyze.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var birthDate by mutableStateOf("")
}
