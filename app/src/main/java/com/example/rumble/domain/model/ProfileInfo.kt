package com.example.rumble.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileInfo(
    val accessToken: String,
    val activeSubscriptions: List<String>,
    val phone: String,
    val firstname: String,
    val address: AddressInfo,
    val userId: Int,
    val email: String,
    val lastname: String,
): Parcelable

@Parcelize
data class AddressInfo(
    val zip: String,
    val city: String,
    val street: String
):Parcelable
