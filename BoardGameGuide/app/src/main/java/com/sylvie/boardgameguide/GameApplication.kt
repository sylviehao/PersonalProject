package com.sylvie.boardgameguide

import android.app.Application
import android.content.Context
import android.util.Log
import kotlin.properties.Delegates

class GameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
    companion object {
        var appContext: Context? = null
            private set
    }
}