package com.example.rumble.domain

import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.domain.usecases.ArticlesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ArticlesUseCaseTest {

    private lateinit var articlesUseCase: ArticlesUseCase
    private val repository = mockk<RumbleRepository>(relaxed = true)

    @Before
    fun setUp() {
        articlesUseCase = ArticlesUseCase(repository)
    }

    @Test
    fun `should load articles successfully`() = runTest {
        //Given
        coEvery { repository.getArticles("Token") } returns Result.Success(listOf(fakeArticleInfo))

        //When
        val result = articlesUseCase.invoke("Token")

        //Then
        Assert.assertEquals(result, Result.Success(listOf(fakeArticleInfo)))
    }

    @Test
    fun `should load articles failed`() = runTest {
        //Given
        coEvery { repository.getArticles("Token") } returns Result.Error(Exception("Failed loading article"))

        //When
        val result = articlesUseCase.invoke("Token")

        //Then
        val errorResult = result as Result.Error
        Assert.assertEquals("Failed loading article", errorResult.exception.message)
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