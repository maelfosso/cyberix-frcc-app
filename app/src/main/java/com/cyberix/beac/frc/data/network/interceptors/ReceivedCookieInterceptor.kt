package com.cyberix.beac.frc.data.network.interceptors

import com.cyberix.beac.frc.utils.CookieManager
import okhttp3.Interceptor
import okhttp3.Response

class ReceivedCookiesInterceptor(private val cookieManager: CookieManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val cookies = originalResponse.headers("Set-Cookie")
//        if (cookies.isNotEmpty()) {
//            cookieManager.saveCookies(cookies)
//        }
        if (cookies.isNotEmpty()) {
            val jwtCookie = cookies.find { it.startsWith("jwt=") }
            jwtCookie?.let {
                val jwtValue = it.substringAfter("jwt=")
                cookieManager.saveCookies("jwt", listOf(jwtValue))
            }
        }

        return originalResponse
    }
}
