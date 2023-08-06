package com.example.rumble.repositories

import com.example.rumble.domain.common.Result
import com.example.rumble.infrastructure.api.ArticleResponse
import com.example.rumble.infrastructure.api.LoginCredentials
import com.example.rumble.infrastructure.api.LoginResponse
import com.example.rumble.infrastructure.api.RumbleApi
import com.example.rumble.infrastructure.repositories.RumbleRemoteDataSource
import com.example.rumble.infrastructure.repositories.RumbleRemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RumbleRemoteDataSourceImplTest {

    private lateinit var remoteDataSource: RumbleRemoteDataSource
    private val service = mockk<RumbleApi>(relaxed = true)
    private val loginResponse = mockk<LoginResponse>()

    @Before
    fun setUp() {
        remoteDataSource = RumbleRemoteDataSourceImpl(
            service = service,
        )
    }

    @Test
    fun `should return token successfully`() = runTest {
        // Given
        val response = Response.success(loginResponse)
        coEvery { service.loginViaCredential(LoginCredentials("username","password")) } returns response

        // When
        val result = remoteDataSource.getToken(LoginCredentials("username","password"))

        // Then
        assertEquals(loginResponse, result)
    }

    @Test(expected = IOException::class)
    fun `should failed to return token`() = runTest {
        // Given
        val errorMessage = "Error message"
        coEvery { service.loginViaCredential(LoginCredentials("username","password")) } throws IOException(errorMessage)

        // When
        val result = remoteDataSource.getToken(LoginCredentials("username","password"))

        // Then
        assertEquals(Result.Error(Exception(errorMessage)), result)
    }

    @Test
    fun `should authorize Token successfully`() = runTest {
        // Given
        val response = Response.success(loginResponse)
        coEvery { service.authorizeToken(any()) } returns response

        // When
        val result = remoteDataSource.authorizeToken("Bearer ".plus("Token"))

        // Then
        assertEquals(loginResponse, result)
    }

    @Test(expected = IOException::class)
    fun `should failed to authorize Token`() = runTest {
        // Given
        val errorMessage = "Error message"
        coEvery { service.authorizeToken(any()) } throws IOException(errorMessage)

        // When
        val result = remoteDataSource.authorizeToken("Token")

        // Then
        assertEquals(Result.Error(Exception(errorMessage)), result)
    }

    @Test
    fun `should return articles successfully`() = runTest {
        // Given
        val responseList = listOf(fakeArticleInfo)
        val response = Response.success(responseList)
        coEvery { service.getArticles(any()) } returns response

        // When
        val result = remoteDataSource.getArticles("Token")

        // Then
        assertEquals(listOf(fakeArticleInfo), result)
    }

    @Test(expected = IOException::class)
    fun `should failed return articles`() = runTest {
        // Given
        val errorMessage = "Error message"
        coEvery { service.getArticles(any()) } throws IOException(errorMessage)

        // When
        val result = remoteDataSource.getArticles("Token")

        // Then
        assertEquals(Result.Error(Exception(errorMessage)), result)
    }

    @Test
    fun `should return article's details successfully`() = runTest {
        // Given
        val responseList = fakeArticleInfo
        val response = Response.success(responseList)
        coEvery { service.getArticleDetails(any(),100) } returns response

        // When
        val result = remoteDataSource.getArticleDetails("Token",100)

        // Then
        assertEquals(fakeArticleInfo, result)
    }

    @Test(expected = IOException::class)
    fun `should failed return article's details `() = runTest {
        // Given
        val errorMessage = "Error message"
        coEvery { service.getArticleDetails(any(), 100) } throws IOException(errorMessage)

        // When
        val result = remoteDataSource.getArticleDetails("Token", 100)

        // Then
        assertEquals(Result.Error(Exception(errorMessage)), result)
    }

    private val fakeArticleInfo = ArticleResponse(
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