package com.cyberix.beac.frc.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesCookieManager(context: Context) : CookieManager {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("cookies", Context.MODE_PRIVATE)

    override fun saveCookies(name: String, cookies: List<String>) {
        val editor = sharedPrefs.edit()
        cookies.forEach { cookie ->
            editor.putString(name, cookie)
        }
        editor.apply()
    }

    override fun getCookie(name: String): String? = sharedPrefs.getString(name, null)
}
