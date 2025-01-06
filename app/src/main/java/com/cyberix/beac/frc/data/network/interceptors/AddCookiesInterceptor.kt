package com.cyberix.beac.frc.data.network.interceptors

import com.cyberix.beac.frc.utils.CookieManager
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor(private val cookieManager: CookieManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val cookie = cookieManager.getCookie("jwt")
        if (cookie != null) {
            builder.addHeader("jwt", cookie)
        }

        return chain.proceed(builder.build())
    }
}