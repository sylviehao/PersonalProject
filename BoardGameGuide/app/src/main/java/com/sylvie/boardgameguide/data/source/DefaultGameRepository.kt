package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.LiveData
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultGameRepository (private val gameRemoteDataSource: GameDataSource,
                                      private val gameLocalDataSource: GameDataSource,
                                      private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {
    override suspend fun getEvents(id : String): Result<List<Event>> {
        return gameRemoteDataSource.getEvents(id)
    }

    override suspend fun getGame(id: String): Result<List<Game>> {
        return gameRemoteDataSource.getGame(id)
    }
}