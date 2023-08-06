package com.example.rumble.domain

import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.AddressInfo
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.domain.usecases.AuthorizeTokenUseCase
import com.example.rumble.infrastructure.api.LoginCredentials
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthorizeTokenUseCaseTest {

    private lateinit var authorizeTokenUseCase: AuthorizeTokenUseCase
    private val repository = mockk<RumbleRepository>(relaxed = true)

    @Before
    fun setUp() {
        authorizeTokenUseCase = AuthorizeTokenUseCase(repository)
    }

    @Test
    fun `should authorize successfully`() = runTest {
        //Given
        coEvery { repository.authorizeToken("Token") } returns Result.Success(fakePersonalInfo)

        //When
        val result = authorizeTokenUseCase.invoke("Token")

        //Then
        assertEquals(result, Result.Success(fakePersonalInfo))
    }

    @Test
    fun `should authorize failed`() = runTest {
        //Given
        coEvery { repository.authorizeToken("Token") } returns Result.Error(Exception("Failed login"))

        //When
        val result = authorizeTokenUseCase.invoke("Token")

        //Then
        val errorResult = result as Result.Error
        assertEquals("Failed login", errorResult.exception.message)
    }

    private val fakePersonalInfo = ProfileInfo(
        accessToken = "Token",
        activeSubscriptions = listOf("sports"),
        phone = "phone",
        firstname = "name",
        address = AddressInfo("zip", "Linz", "street"),
        userId = 100,
        email = "email",
        lastname = "family",
    )
}