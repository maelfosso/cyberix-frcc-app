package com.cyberix.beac.frc.utils

interface CookieManager {
    fun saveCookies(name: String, cookies: List<String>)
    fun getCookie(name: String): String?
}
