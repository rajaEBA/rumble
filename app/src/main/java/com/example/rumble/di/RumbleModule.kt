package com.example.rumble.di

import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.domain.usecases.ArticleDetailsUseCase
import com.example.rumble.domain.usecases.ArticlesUseCase
import com.example.rumble.domain.usecases.AuthorizeTokenUseCase
import com.example.rumble.domain.usecases.LoginViaCredentialUseCase
import com.example.rumble.infrastructure.api.ApiExecutor
import com.example.rumble.infrastructure.api.ApiExecutor.Rumble_APIS_ENDPOINT
import com.example.rumble.infrastructure.repositories.RumbleRemoteDataSource
import com.example.rumble.infrastructure.repositories.RumbleRemoteDataSourceImpl
import com.example.rumble.infrastructure.repositories.RumbleRepositoryImpl
import com.example.rumble.infrastructure.utils.NetworkManager
import com.example.rumble.presentation.ui.article.ArticleViewModel
import com.example.rumble.presentation.ui.details.ArticleDetailsViewModel
import com.example.rumble.presentation.ui.login.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun rumbleDependencies(): List<Module> {
    return listOf(nounDigitalModule())
}

private fun nounDigitalModule() = module {

    val httpClient = ApiExecutor.createApi(Rumble_APIS_ENDPOINT)
    single<RumbleRemoteDataSource> {
        RumbleRemoteDataSourceImpl(
            service = httpClient,
        )
    }

    single { NetworkManager(androidApplication()) }

    single<RumbleRepository>(createdAtStart = true) {
        RumbleRepositoryImpl(
            remoteDataSource = get(),
            networkManager = get(),
        )
    }

    factory {
        LoginViaCredentialUseCase(
            repository = get()
        )
    }

    factory {
        AuthorizeTokenUseCase(
            repository = get()
        )
    }

    factory {
        ArticlesUseCase(
            repository = get()
        )
    }

    viewModel {
        LoginViewModel(
            loginViaCredentialUseCase = get(),
            authorizeTokenUseCase = get()
        )
    }

    viewModel {
        ArticleViewModel(
            context = androidApplication(),
            articlesUseCase = get(),
        )
    }

    factory {
        ArticleDetailsUseCase(
            repository = get()
        )
    }

    viewModel {
        ArticleDetailsViewModel(
            articleDetailsUseCase = get(),
        )
    }
}