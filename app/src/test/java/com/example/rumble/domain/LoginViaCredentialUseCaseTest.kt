package com.example.rumble.domain

import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.AddressInfo
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.domain.usecases.LoginViaCredentialUseCase
import com.example.rumble.infrastructure.api.LoginCredentials
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class LoginViaCredentialUseCaseTest {

    private lateinit var loginViaCredentialUseCase: LoginViaCredentialUseCase
    private val repository = mockk<RumbleRepository>(relaxed = true)

    @Before
    fun setUp() {
        loginViaCredentialUseCase = LoginViaCredentialUseCase(repository)
    }

    @Test
    fun `should login successfully`() = runTest {
        //Given
        coEvery {
            repository.login(
                LoginCredentials(
                    "Username",
                    "Password"
                )
            )
        } returns Result.Success(fakePersonalInfo)

        //When
        val result = loginViaCredentialUseCase.invoke(LoginCredentials("Username", "Password"))

        //Then
        assertEquals(result, Result.Success(fakePersonalInfo))
    }

    @Test
    fun `should login failed`() = runTest {
        //Given
        coEvery { repository.login(LoginCredentials("Username","Password")) } returns Result.Error(Exception("Failed login"))

        //When
        val result = loginViaCredentialUseCase.invoke(LoginCredentials("Username", "Password"))

        //Then
        assertTrue(result is Result.Error)
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