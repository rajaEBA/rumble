package com.example.rumble.presentation.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//fun SharedPreferences.putString(key: String, value: String) {
//    val jsonString = Gson().toJson(value)
//    edit().putString(key, jsonString).apply()
//}
//
//fun Context.storeToken(key: String, value: String) {
//    val sharedPreferences = getSharedPreferences("PREFERENCE_RUMBLE", Context.MODE_PRIVATE)
//    sharedPreferences.putString(key, value)
//}
//
//fun Context.getToken(key:String): String? {
//    val sharedPreferences = getSharedPreferences("PREFERENCE_RUMBLE", Context.MODE_PRIVATE)
//    return sharedPreferences.retrieveString(key)
//}
//
//fun SharedPreferences.retrieveString(key: String): String? {
//    val jsonString = getString(key, null)
//    return if (jsonString != null) {
//        Gson().fromJson(jsonString, String::class.java)
//    } else {
//        null
//    }
//}

fun SharedPreferences.putValue(key: String, value: Any) {
    val jsonString = Gson().toJson(value)
    edit().putString(key, jsonString).apply()
}

fun Context.storeValue(key: String, value: Any) {
    val sharedPreferences = getSharedPreferences("PREFERENCE_RUMBLE", Context.MODE_PRIVATE)
    sharedPreferences.putValue(key, value)
}

inline fun <reified T> Context.getValue(key: String): T? {
    val sharedPreferences = getSharedPreferences("PREFERENCE_RUMBLE", Context.MODE_PRIVATE)
    return sharedPreferences.retrieveValue(key)
}

inline fun <reified T> SharedPreferences.retrieveValue(key: String): T? {
    val jsonString = getString(key, null)
    return if (jsonString != null) {
        Gson().fromJson(jsonString, object : TypeToken<T>() {}.type)
    } else {
        null
    }
}