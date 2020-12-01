package com.sylvie.boardgameguide.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.sylvie.boardgameguide.data.source.DefaultGameRepository
import com.sylvie.boardgameguide.data.source.GameDataSource
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.data.source.local.GameLocalDataSource
import com.sylvie.boardgameguide.data.source.remote.GameRemoteDataSource

object ServiceLocator {

    @Volatile
    var gameRepository: GameRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): GameRepository {
        synchronized(this) {
            return gameRepository
                ?: gameRepository
                ?: createGameRepository(context)
        }
    }

    private fun createGameRepository(context: Context): GameRepository {
        return DefaultGameRepository(GameRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): GameDataSource {
        return GameLocalDataSource(context)
    }
}