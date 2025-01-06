package com.cyberix.beac.frc.ui.start.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberix.beac.frc.data.network.ApiState
import com.cyberix.beac.frc.data.network.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    private val _requestState =
        MutableStateFlow<ApiState<Boolean>>(ApiState.Empty)
    val requestState = _requestState.asStateFlow()

    init {
        resetLoginForm()
    }

    private fun resetLoginForm() {
        _uiState.value = LoginUIState()
    }

    fun updateForm(newState: LoginUIState) {
        _uiState.update { newState }
    }

    fun submit() {
        _requestState.value = ApiState.Loading

        viewModelScope.launch {
            try {
                val response = authService.login(uiState.value.toLoginRequest())
                _requestState.value = ApiState.Success(response)
            } catch(e: Exception) {
                _requestState.value = ApiState.Error(404, e.message.toString())
            }
        }
    }
}