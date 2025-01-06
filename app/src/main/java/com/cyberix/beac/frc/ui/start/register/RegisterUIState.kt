package com.cyberix.beac.frc.ui.start.register

import com.cyberix.beac.frc.data.repository.auth.RegisterRequest

enum class Quality {
    Expert,
    Participant
}

data class RegisterUIState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val quality: String = Quality.Participant.name,
    val phone: String = "",
    val organization: String = ""
) {
    fun isNotEmpty(): Boolean {
        return lastName.isNotEmpty() && firstName.isNotEmpty() &&
                email.isNotEmpty() && phone.isNotEmpty() &&
                phone.isNotEmpty() && organization.isNotEmpty()
    }

    fun toRegisterRequest(): RegisterRequest {
        return RegisterRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone,
            quality = quality,
            organization = organization
        )
    }
}