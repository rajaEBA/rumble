package com.example.rumble.repositories

import com.example.rumble.infrastructure.api.ArticleResponse
import com.example.rumble.infrastructure.mappers.toArticle
import com.example.rumble.infrastructure.repositories.RumbleRemoteDataSource
import com.example.rumble.infrastructure.repositories.RumbleRepositoryImpl
import com.example.rumble.infrastructure.utils.NetworkManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.example.rumble.domain.common.Result
import com.example.rumble.infrastructure.api.Address
import com.example.rumble.infrastructure.api.LoginCredentials
import com.example.rumble.infrastructure.api.LoginResponse
import com.example.rumble.infrastructure.mappers.toProfileInfo
import okio.IOException
import org.junit.Assert

@ExperimentalCoroutinesApi
class RumbleRepositoryImplTest {

    private var remoteDataSource = mockk<RumbleRemoteDataSource>()
    private var networkManager = mockk<NetworkManager>()

    private lateinit var rumbleRepository: RumbleRepositoryImpl

    @Before
    fun setup() {
        rumbleRepository = RumbleRepositoryImpl(remoteDataSource, networkManager)
    }

    @Test
    fun `should fetch articles with network connection`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns true
        val remoteArticles = listOf(fakeArticleInfo)

        val mappedAsset = remoteArticles.map{ it.toArticle() }
        coEvery { remoteDataSource.getArticles(any()) } returns remoteArticles

        //When
        val result = rumbleRepository.getArticles("Token")

        //Then
        assertEquals(Result.Success(mappedAsset), result)
        coVerify { remoteDataSource.getArticles("Token") }
    }

    @Test
    fun `should fetch articles details with network connection`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns true


        coEvery { remoteDataSource.getArticleDetails(any(),100) } returns fakeArticleInfo

        //When
        val result = rumbleRepository.getArticleDetails("Token",100)

        //Then
        assertEquals(Result.Success(fakeArticleInfo.toArticle()), result)
        coVerify { remoteDataSource.getArticleDetails("Token",100) }
    }

    @Test
    fun `should login with network connection`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns true
        coEvery { remoteDataSource.getToken(LoginCredentials("username","password")) } returns fakeLoginResponse

        //When
        val result = rumbleRepository.login(LoginCredentials("username","password"))

        //Then

        assertEquals(Result.Success(fakeLoginResponse.toProfileInfo()), result)
        coVerify { remoteDataSource.getToken(LoginCredentials("username","password")) }
    }

    @Test
    fun `should authorize token with network connection`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns true
        coEvery { remoteDataSource.authorizeToken(any()) } returns fakeLoginResponse

        //When
        val result = rumbleRepository.authorizeToken("Token")

        //Then
        assertEquals(Result.Success(fakeLoginResponse.toProfileInfo()), result)
        coVerify { remoteDataSource.authorizeToken("Token") }
    }

    @Test
    fun `should return no network connectivity error`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns false
        coEvery { remoteDataSource.getArticles(any()) } returns emptyList()

        //When
        val result = rumbleRepository.getArticles("Token")

        //Then
        Assert.assertTrue(result is Result.Error && result.exception.message == "No Network Connectivity")
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

    private val fakeLoginResponse = LoginResponse(
        accessToken = "Token",
        activeSubscriptions = listOf("sports"),
        phone = "phone",
        fisrtname = "name",
        adress = Address("zip", "Linz", "street"),
        userId = 100,
        email = "email",
        lastname = "family",
    )
}