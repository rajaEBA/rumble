package com.example.rumble.infrastructure.mappers

import com.example.rumble.domain.model.AddressInfo
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.infrastructure.api.ArticleResponse
import com.example.rumble.infrastructure.api.LoginResponse

fun LoginResponse.toProfileInfo(): ProfileInfo {
    return ProfileInfo(
        accessToken = this.accessToken,
        activeSubscriptions = this.activeSubscriptions,
        phone = this.phone,
        firstname = this.fisrtname,
        address = AddressInfo(this.adress.zip,this.adress.city,this.adress.street),
        userId = this.userId,
        email = this.email,
        lastname = this.lastname,
    )
}

fun ArticleResponse.toArticle(): Article {
    return Article(
        id = this.id,
        title1 = this.title1,
        title2 = this.title2,
        imageCaption = this.imageCaption,
        datePublished = this.datePublished,
        keyword1 = this.keyword1,
        keyword2 = this.keyword2,
        authorName = this.authorName,
        imageCopyright = this.imageCopyright,
        imageURL = this.imageURL,
        subscriptionType = this.subscriptionType,
        contentHTML = this.contentHTML,
    )
}