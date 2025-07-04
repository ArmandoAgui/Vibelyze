package com.vibelyze.ui.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {
    private val _hasFinishedOnboarding = MutableStateFlow(false)
    val hasFinishedOnboarding: StateFlow<Boolean> get() = _hasFinishedOnboarding

    fun finishOnboarding() {
        _hasFinishedOnboarding.value = true
    }
}
