package com.example.rumble.domain.model

data class Article(
    val id: Long,
    val title1: String? = null,
    val title2: String? = null,
    val imageCaption: String? = null,
    val datePublished: String? = null,
    val keyword1: String? = null,
    val keyword2: String? = null,
    val authorName: String? = null,
    val imageCopyright: String? = null,
    val imageURL: String? = null,
    val subscriptionType: String,
    val contentHTML: String? = null,
)