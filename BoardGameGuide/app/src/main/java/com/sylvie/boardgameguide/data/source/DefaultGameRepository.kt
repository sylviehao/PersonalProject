package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.LiveData
import com.sylvie.boardgameguide.data.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultGameRepository (private val gameRemoteDataSource: GameDataSource,
                                      private val gameLocalDataSource: GameDataSource,
                                      private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {
    override suspend fun getEvent(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

}