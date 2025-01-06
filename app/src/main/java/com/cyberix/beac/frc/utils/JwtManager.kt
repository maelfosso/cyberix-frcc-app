package com.cyberix.beac.frc.utils
import android.content.Context
import android.util.Log
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JWTManager @Inject constructor(private val cookieManager: CookieManager) {

    suspend fun isJwtValid(): Boolean {
        val jwtString = getJwtString()

        Log.d("JWTManager", "JWT : ${jwtString}")

        return if (!jwtString.isNullOrEmpty()) {
            try {
                val jwt = JWT(jwtString)
                Log.d("JWTManager", "JWT : ${jwt.isExpired(0)}")
                
                !jwt.isExpired(0)
            } catch (e: Exception) {
                e.message?.let { Log.e("JWTManager", it) }
                false
            }
        } else {
            false
        }
    }

    private fun getJwtString(): String? {
        return cookieManager.getCookie("jwt")
    }
}
