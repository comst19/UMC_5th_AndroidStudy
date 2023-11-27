package com.comst.flocloneapp.data.network

import com.comst.flocloneapp.model.local.UserEntity
import com.comst.flocloneapp.model.network.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FLOAuthInterface {

    @POST("/users")
    suspend fun signUp(@Body user : UserEntity): Response<AuthResponse>

    @POST("/users/login")
    suspend fun login(@Body user : UserEntity): Response<AuthResponse>

    @GET("/users/auto-login")
    suspend fun autoLogin(@Header("X-ACCESS-TOKEN") jwt : String ) : Response<AuthResponse>

}
