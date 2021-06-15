package com.practice.searchengine

import android.content.Context
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {
    lateinit var appComponent: AppComponent

    val appContext: Context
        get() = this.applicationContext

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.create()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}