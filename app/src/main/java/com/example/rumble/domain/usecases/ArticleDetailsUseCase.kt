package com.example.rumble.domain.usecases

import com.example.rumble.domain.outport.RumbleRepository

class ArticleDetailsUseCase(
    private val repository: RumbleRepository
) {
    suspend operator fun invoke(token: String, articleId: Long) =
        repository.getArticleDetails(token, articleId)
}