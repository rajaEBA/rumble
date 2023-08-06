package com.example.rumble

import android.app.Application
import com.example.rumble.di.rumbleDependencies
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class RumbleApplication : Application() {

    private val koinApp by lazy {
        startKoin {
            androidContext(this@RumbleApplication)
        }
    }

    override fun onCreate() {
        super.onCreate()
        koinApp.koin.loadModules(koinModuleList())
    }

    private fun koinModuleList() = ArrayList<Module>().apply {
        addAll(rumbleDependencies())
    }
}