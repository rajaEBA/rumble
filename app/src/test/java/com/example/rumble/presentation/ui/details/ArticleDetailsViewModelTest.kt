package com.example.rumble.presentation.ui.details

import app.cash.turbine.test
import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.usecases.ArticleDetailsUseCase
import com.example.rumble.presentation.ui.details.ArticleDetailsViewModel.ArticleDetailsState.DetailsLoaded
import com.example.rumble.presentation.ui.details.ArticleDetailsViewModel.ArticleDetailsState.LoadingDetailsFailed
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

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleDetailsViewModelTest {

    private lateinit var viewModel: ArticleDetailsViewModel
    private val articleDetailsUseCase = mockk<ArticleDetailsUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `should show details`() = runTest {
        //Given
        coEvery { articleDetailsUseCase.invoke(any(), 100) } coAnswers { Result.Success(fakeArticleInfo) }

        viewModel = ArticleDetailsViewModel(articleDetailsUseCase)
        viewModel.getDetails("Token", 100)

        viewModel.articleDetailsViewState.test {
            assertThat(awaitItem()).isEqualTo(DetailsLoaded(fakeArticleInfo))
        }
    }

    @Test
    fun `should failed to show details`() = runTest {
        //Given
        coEvery { articleDetailsUseCase.invoke(any(), 100) } coAnswers { Result.Error(Exception("Failed loading article details")) }

        viewModel = ArticleDetailsViewModel(articleDetailsUseCase)
        viewModel.getDetails("Token", 100)

        viewModel.articleDetailsViewState.test {
            assertThat(awaitItem()).isEqualTo(LoadingDetailsFailed("Failed loading article details")
            )
        }
    }

    private val fakeArticleInfo = Article(
        id = 100,
        title1 = "title1",
        title2 = "title2",
        imageCaption = "imageCaption",
        datePublished = "datePublished",
        keyword1 = "keyword1",
        keyword2 = "keyword2",
        authorName = "authorName",
        imageCopyright = "imageCopyright",
        imageURL = "imageURL",
        subscriptionType = "subscriptionType",
        contentHTML = "contentHTML",
    )
}