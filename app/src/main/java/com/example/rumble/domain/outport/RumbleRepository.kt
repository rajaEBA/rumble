package com.example.rumble.domain.outport

import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.Article
import com.example.rumble.infrastructure.api.LoginCredentials

interface RumbleRepository {

    suspend fun login(credentials: LoginCredentials): Result<ProfileInfo>

    suspend fun authorizeToken(token: String): Result<ProfileInfo>

    suspend fun getArticles(token: String): Result<List<Article>>

    suspend fun getArticleDetails(token:String, articleId:Long): Result<Article>
}