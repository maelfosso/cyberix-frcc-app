package com.cyberix.beac.frc.data.repository.auth

data class OtpRequest (
    val email: String = "",
    val otp: String = ""
)