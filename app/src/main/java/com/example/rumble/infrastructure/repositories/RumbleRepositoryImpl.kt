package com.example.rumble.infrastructure.repositories

import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.infrastructure.api.LoginCredentials
import com.example.rumble.infrastructure.mappers.toArticle
import com.example.rumble.infrastructure.mappers.toProfileInfo
import com.example.rumble.infrastructure.utils.NetworkManager

class RumbleRepositoryImpl(
    private val remoteDataSource: RumbleRemoteDataSource,
    private val networkManager: NetworkManager,
) : RumbleRepository {

    override suspend fun login(credentials: LoginCredentials): Result<ProfileInfo> {
        return if (networkManager.hasConnection()) {
            try {
                val response = remoteDataSource.getToken(credentials)
                val profile = response.toProfileInfo()
                Result.Success(profile)
            } catch (e: Exception) {
                Result.Error(e)
            }
        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }

    override suspend fun authorizeToken(token: String): Result<ProfileInfo> {
        return if (networkManager.hasConnection()) {
            try {
                val response = remoteDataSource.authorizeToken(token)
                val profile = response.toProfileInfo()
                Result.Success(profile)
            } catch (e: Exception) {
                Result.Error(e)
            }
        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }

    override suspend fun getArticles(token: String): Result<List<Article>> {
        return if (networkManager.hasConnection()) {
            try {
                val response = remoteDataSource.getArticles(token)
                val articles = response.map { it.toArticle() }
                Result.Success(articles)
            } catch (e: Exception) {
                Result.Error(e)
            }
        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }

    override suspend fun getArticleDetails(token: String, articleId: Long): Result<Article> {
        return if (networkManager.hasConnection()) {
            try {
                val response = remoteDataSource.getArticleDetails(token, articleId)
                val details = response.toArticle()
                Result.Success(details)
            } catch (e: Exception) {
                Result.Error(e)
            }
        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }

}