package com.example.rumble.infrastructure.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("active_subscriptions")
    val activeSubscriptions: List<String>,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("fisrtname")
    val fisrtname: String,
    @SerializedName("adress")
    val adress: Address,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("lastname")
    val lastname: String,
)

data class Address(
    @SerializedName("zip")
    val zip: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("street")
    val street: String
)
