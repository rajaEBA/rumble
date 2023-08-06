package com.example.rumble.infrastructure.api

import retrofit2.Response
import retrofit2.http.*

interface RumbleApi {

    @POST("users/login")
    suspend fun loginViaCredential(@Body credentials: LoginCredentials): Response<LoginResponse>

    @GET("users/me")
    suspend fun authorizeToken(@Header("Authorization") token: String): Response<LoginResponse>

    @GET("Articles")
    suspend fun getArticles(@Header("Authorization") token: String): Response<List<ArticleResponse>>

    @GET("Articles/{asset_id}")
    suspend fun getArticleDetails(@Header("Authorization") token: String, @Path("asset_id") articleId: Long): Response<ArticleResponse>
}

data class LoginCredentials(
    val email: String,
    val password: String
)