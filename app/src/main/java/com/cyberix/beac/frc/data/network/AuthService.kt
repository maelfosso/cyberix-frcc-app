package com.cyberix.beac.frc.data.network;

import com.cyberix.beac.frc.data.repository.auth.LoginRequest
import com.cyberix.beac.frc.data.repository.auth.LoginResponse
import com.cyberix.beac.frc.data.repository.auth.OtpRequest
import com.cyberix.beac.frc.data.repository.auth.RegisterRequest
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface AuthService {
    @POST("/auth/register")
    suspend fun register(@Body data: RegisterRequest): Boolean

    @POST("/auth/login")
    suspend fun login(@Body data: LoginRequest): Boolean

    @POST("/auth/otp")
    suspend fun otp(@Body data: OtpRequest): Boolean

//    @POST("/auth/verify/{token}")
//    suspend fun verify(@Path("token") token String): Response<Boolean>

}
