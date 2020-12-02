package com.sylvie.boardgameguide

import android.app.Application
import android.content.Context
import android.util.Log
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.util.ServiceLocator
import kotlin.properties.Delegates

class GameApplication : Application() {

    // Depends on the flavor
    val gameRepository: GameRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
    companion object {
        var appContext: Context? = null
            private set
    }
}