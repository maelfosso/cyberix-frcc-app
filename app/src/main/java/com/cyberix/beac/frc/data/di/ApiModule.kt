package com.cyberix.beac.frc.data.di

import android.content.Context
import com.cyberix.beac.frc.AppConfig
import com.cyberix.beac.frc.BuildConfig
import com.cyberix.beac.frc.data.network.AuthService
import com.cyberix.beac.frc.data.network.interceptors.AddCookiesInterceptor
import com.cyberix.beac.frc.data.network.interceptors.ReceivedCookiesInterceptor
import com.cyberix.beac.frc.loadConfig
import com.cyberix.beac.frc.utils.CookieManager
import com.cyberix.beac.frc.utils.JWTManager
import com.cyberix.beac.frc.utils.SharedPreferencesCookieManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAppConfig(@ApplicationContext context: Context): AppConfig {
        return loadConfig(context, BuildConfig.CONFIG_FILE) ?: AppConfig(
            "http://10.0.2.2:8080/",
        )
    }

    @Singleton
    @Provides
    fun providesCookieManager(@ApplicationContext context: Context): CookieManager =
        SharedPreferencesCookieManager(context)

    @Provides
    @Singleton
    fun provideJWTManager(cookieManager: CookieManager): JWTManager {
        return JWTManager(cookieManager)
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cookieManager: CookieManager
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ReceivedCookiesInterceptor(cookieManager))
            .addInterceptor(AddCookiesInterceptor(cookieManager))
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, appConfig: AppConfig): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(appConfig.baseUrl)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit
            .create(AuthService::class.java)
}