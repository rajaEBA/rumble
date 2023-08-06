package com.example.rumble.domain.usecases

import com.example.rumble.domain.outport.RumbleRepository

class ArticlesUseCase(
    private val repository: RumbleRepository
) {
    suspend operator fun invoke(token: String) = repository.getArticles(token)
}