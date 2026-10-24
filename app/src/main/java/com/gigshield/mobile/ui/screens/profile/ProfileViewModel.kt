package com.gigshield.mobile.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.core.common.Constants
import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.core.preferences.SessionManager
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val serverMode: String = Constants.MODE_CLOUD,
    val customUrl: String = "",
    val isDarkMode: Boolean = true,
    val isLoading: Boolean = false,
    val updateMessage: String? = null,
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            serverMode = sessionManager.getServerMode(),
            customUrl = sessionManager.getCustomUrl() ?: "",
            isDarkMode = sessionManager.isDarkMode()
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    fun updateProfile(name: String, city: String, platform: String, zone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, updateMessage = null, errorMessage = null) }
            when (val res = authRepository.updateProfile(name, city, platform, zone)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = res.data,
                            updateMessage = "Profile updated & AI risk recalculated!"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = res.message)
                    }
                }
                else -> Unit
            }
        }
    }

    fun setServerMode(mode: String) {
        sessionManager.setServerMode(mode)
        _uiState.update { it.copy(serverMode = mode) }
    }

    fun setCustomUrl(url: String) {
        sessionManager.setCustomUrl(url)
        _uiState.update { it.copy(customUrl = url) }
    }

    fun setDarkMode(dark: Boolean) {
        sessionManager.setDarkMode(dark)
        _uiState.update { it.copy(isDarkMode = dark) }
    }

    fun logout() {
        authRepository.logout()
    }
}
