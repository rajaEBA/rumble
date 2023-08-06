package com.example.rumble.presentation.ui.login

import app.cash.turbine.test
import com.example.rumble.domain.model.AddressInfo
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.domain.usecases.AuthorizeTokenUseCase
import com.example.rumble.domain.usecases.LoginViaCredentialUseCase
import com.example.rumble.infrastructure.api.LoginCredentials
import com.example.rumble.presentation.ui.login.LoginViewModel.LoginState.LunchMain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import com.example.rumble.domain.common.Result

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginViaCredentialUseCase = mockk<LoginViaCredentialUseCase>()
    private val authorizeTokenUseCase = mockk<AuthorizeTokenUseCase>()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = LoginViewModel(loginViaCredentialUseCase, authorizeTokenUseCase)
    }

    @Test
    fun `should login via credential`() = runTest {
        //Given
        coEvery {
            loginViaCredentialUseCase.invoke(
                LoginCredentials(
                    "username",
                    "password"
                )
            )
        } coAnswers { Result.Success(fakePersonalInfo) }
        coEvery { authorizeTokenUseCase.invoke(any()) } returns Result.Success(fakePersonalInfo)

        //When + Then
        viewModel.loginViewState.test {
            viewModel.onEvent(LoginViewModel.Event.Login(LoginCredentials("username", "password")))

            assertThat(awaitItem()).isEqualTo(LunchMain(fakePersonalInfo))
        }
    }

    @Test
    fun `should failed login via credential`() = runTest {
        //Given
        coEvery {
            loginViaCredentialUseCase.invoke(
                LoginCredentials(
                    "username",
                    "password"
                )
            )
        } coAnswers { Result.Error(Exception("Failed login")) }
        coEvery { authorizeTokenUseCase.invoke(any()) } returns Result.Success(fakePersonalInfo)

        //When + Then
        viewModel.loginViewState.test {
            viewModel.onEvent(LoginViewModel.Event.Login(LoginCredentials("username", "password")))

            assertThat(awaitItem()).isEqualTo(LoginViewModel.LoginState.FailedLogin("Failed login"))
        }
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