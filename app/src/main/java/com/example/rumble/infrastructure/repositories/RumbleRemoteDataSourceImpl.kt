package com.example.rumble.infrastructure.repositories

import com.example.rumble.infrastructure.api.*
import okio.IOException

class RumbleRemoteDataSourceImpl(
    private val service: RumbleApi,
) : RumbleRemoteDataSource {

    override suspend fun getToken(credentials: LoginCredentials): LoginResponse {
        return try {
            val response = service.loginViaCredential(credentials)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw IOException(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun authorizeToken(token: String): LoginResponse {
        return try {
            val response = service.authorizeToken("Bearer ".plus(token))
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw IOException(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getArticles(token: String): List<ArticleResponse> {
        return try {
            val response = service.getArticles("Bearer ".plus(token))
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw IOException(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getArticleDetails(token: String, articleId: Long): ArticleResponse {
        return try {
            val response = service.getArticleDetails("Bearer ".plus(token), articleId)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw IOException(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

}

interface RumbleRemoteDataSource {
    suspend fun getToken(credentials: LoginCredentials): LoginResponse
    suspend fun authorizeToken(token: String): LoginResponse
    suspend fun getArticles(token: String): List<ArticleResponse>
    suspend fun getArticleDetails(token: String, articleId: Long): ArticleResponse
}