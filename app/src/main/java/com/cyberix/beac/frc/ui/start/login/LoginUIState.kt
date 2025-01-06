package com.cyberix.beac.frc.ui.start.login

import com.cyberix.beac.frc.data.repository.auth.LoginRequest


data class LoginUIState (
    var email: String = ""
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty()
    }

    fun toLoginRequest(): LoginRequest {
        return LoginRequest(
            email = email,
        )
    }
}