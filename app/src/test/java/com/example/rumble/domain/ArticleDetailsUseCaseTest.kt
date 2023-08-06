package com.example.rumble.domain

import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.outport.RumbleRepository
import com.example.rumble.domain.usecases.ArticleDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ArticleDetailsUseCaseTest {

    private lateinit var articleDetailsUseCase: ArticleDetailsUseCase
    private val repository = mockk<RumbleRepository>(relaxed = true)

    @Before
    fun setUp() {
        articleDetailsUseCase = ArticleDetailsUseCase(repository)
    }

    @Test
    fun `should show details successfully`() = runTest {
        //Given
        coEvery { repository.getArticleDetails("Token", 100) } returns Result.Success(
            fakeArticleInfo
        )

        //When
        val result = articleDetailsUseCase.invoke("Token", 100)

        //Then
        Assert.assertEquals(result, Result.Success(fakeArticleInfo))
    }

    @Test
    fun `should show details failed`() = runTest {
        //Given
        coEvery { repository.getArticleDetails("Token", 100)  } returns Result.Error(Exception("Failed to load Article"))

        //When
        val result = articleDetailsUseCase.invoke("Token", 100)

        //Then
        val errorResult = result as Result.Error
        Assert.assertEquals("Failed to load Article", errorResult.exception.message)
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