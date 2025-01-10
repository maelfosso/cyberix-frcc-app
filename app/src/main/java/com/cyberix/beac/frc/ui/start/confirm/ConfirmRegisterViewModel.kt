package com.cyberix.beac.frc.ui.start.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberix.beac.frc.data.network.ApiState
import com.cyberix.beac.frc.data.network.AuthService
import com.cyberix.beac.frc.data.repository.auth.ConfirmRegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmRegisterViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel()  {
    private val _requestState =
        MutableStateFlow<ApiState<Boolean>>(ApiState.Empty)
    val requestState = _requestState.asStateFlow()

    fun confirm(token : String) {
        _requestState.value = ApiState.Loading

        viewModelScope.launch {
            try {
                val response = authService.confirm(ConfirmRegisterRequest(
                    token = token
                ))
                _requestState.value = ApiState.Success(response)
            } catch(e: Exception) {
                _requestState.value = ApiState.Error(404, e.message.toString())
            }
        }
    }
}