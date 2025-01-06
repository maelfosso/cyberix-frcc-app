package com.cyberix.beac.frc.ui.start.otp

import com.cyberix.beac.frc.data.repository.auth.OtpRequest


data class OtpUIState (
    var email: String = "",
    var digits: MutableList<String> = MutableList(6) { "" },
    var otp: String = ""
) {
    fun isNotEmpty(): Boolean {
        otp = digits.joinToString("")
        return email.isNotEmpty() && otp.isNotEmpty()
    }

    fun toOtpRequest(): OtpRequest {
        return OtpRequest(
            email = email,
            otp = otp
        )
    }
}