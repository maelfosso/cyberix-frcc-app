package com.cyberix.beac.frc

import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

data class AppConfig(val baseUrl: String) // , val apiKey: String, val featureFlagEnabled: Boolean)

fun loadConfig(context: Context, fileName: String): AppConfig? {
    return try {
        val inputStream: InputStream = context.assets.open(fileName)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val jsonString = String(buffer, Charset.forName("UTF-8"))
        Gson().fromJson(jsonString, AppConfig::class.java)
    } catch (e: IOException) {
        e.printStackTrace()
        null // Or handle the exception as needed
    }
}
