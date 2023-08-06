package com.example.rumble.infrastructure.api

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title1")
    val title1: String? = null,
    @SerializedName("title2")
    val title2: String? = null,
    @SerializedName("imageCaption")
    val imageCaption: String? = null,
    @SerializedName("datePublished")
    val datePublished: String? = null,

    @SerializedName("keyword1")
    val keyword1: String? = null,

    @SerializedName("keyword2")
    val keyword2: String? = null,

    @SerializedName("authorName")
    val authorName: String? = null,

    @SerializedName("imageCopyright")
    val imageCopyright: String? = null,

    @SerializedName("imageURL")
    val imageURL: String? = null,

    @SerializedName("subscriptionType")
    val subscriptionType: String,

    @SerializedName("contentHTML")
    val contentHTML: String? = null,
)