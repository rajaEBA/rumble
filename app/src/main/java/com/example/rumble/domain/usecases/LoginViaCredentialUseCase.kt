package com.example.rumble.domain.usecases

import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.infrastructure.api.LoginCredentials

class LoginViaCredentialUseCase(
    private val repository: RumbleRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials) = repository.login(credentials)
}