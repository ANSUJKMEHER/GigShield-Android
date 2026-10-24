package com.gigshield.mobile.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.login(email, pass)) {
                is Resource.Success -> {
                    _uiState.value = AuthUiState(user = result.data, isSuccess = true)
                }
                is Resource.Error -> {
                    _uiState.value = AuthUiState(errorMessage = result.message)
                }
                else -> Unit
            }
        }
    }

    fun signup(
        name: String,
        email: String,
        pass: String,
        city: String,
        platform: String,
        zone: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.signup(
                name = name,
                email = email,
                pass = pass,
                city = city,
                platform = platform,
                zone = zone,
                lat = 19.0760,
                lon = 72.8777
            )) {
                is Resource.Success -> {
                    _uiState.value = AuthUiState(user = result.data, isSuccess = true)
                }
                is Resource.Error -> {
                    _uiState.value = AuthUiState(errorMessage = result.message)
                }
                else -> Unit
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState()
    }
}
