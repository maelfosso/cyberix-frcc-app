package com.cyberix.beac.frc.data.repository.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    val quality: String,
    val phone: String,
    val organization: String
)
