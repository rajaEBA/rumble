package com.example.rumble.presentation.ui.article

import app.cash.turbine.test
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.usecases.ArticlesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import com.example.rumble.domain.common.Result
import com.example.rumble.presentation.ui.article.ArticleViewModel.ArticleState.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {

    private val articlesUseCase = mockk<ArticlesUseCase>()
    private lateinit var viewModel: ArticleViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `should get all articles`() = runTest {
        //Given
        coEvery { articlesUseCase.invoke(any()) } coAnswers { Result.Success(listOf(fakeArticleInfo)) }

        viewModel = ArticleViewModel(articlesUseCase)
        viewModel.getArticles("Token")

        viewModel.assetsViewState.test {
            assertThat(awaitItem()).isEqualTo(ArticlesLoaded(listOf(fakeArticleInfo)))
        }
    }

    @Test
    fun `should failed to get all articles`() = runTest {
        //Given
        coEvery { articlesUseCase.invoke(any()) } coAnswers { Result.Error(Exception("Failed loading article")) }

        viewModel = ArticleViewModel(articlesUseCase)
        viewModel.getArticles("Token")

        viewModel.assetsViewState.test {
            assertThat(awaitItem()).isEqualTo(LoadingArticlesFailed("Failed loading article"))
        }
    }

    @Test
    fun `should show article's details`() = runTest {
        //Given
        coEvery { articlesUseCase.invoke(any()) } coAnswers { Result.Success(listOf(fakeArticleInfo)) }

        viewModel = ArticleViewModel(articlesUseCase)
        viewModel.getArticles("Token")

        viewModel.assetsViewState.test {
            assertThat(awaitItem()).isEqualTo(ArticlesLoaded(listOf(fakeArticleInfo)))
            viewModel.onEvent(ArticleViewModel.Event.ShowDetails(fakeArticleInfo))
            assertThat(awaitItem()).isEqualTo(LoadDetails(fakeArticleInfo))
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