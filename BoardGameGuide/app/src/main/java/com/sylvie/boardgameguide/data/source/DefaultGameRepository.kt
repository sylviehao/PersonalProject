package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultGameRepository (private val gameRemoteDataSource: GameDataSource,
                                      private val gameLocalDataSource: GameDataSource,
                                      private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {
    override suspend fun getHome(): Result<List<HomeItem>> {
        return gameRemoteDataSource.getHome()
    }

    override  fun getEvents(): MutableLiveData<List<Event>> {
        return gameRemoteDataSource.getEvents()
    }

    override suspend fun getGame(id: String): Result<List<Game>> {
        return gameRemoteDataSource.getGame(id)
    }

    override suspend fun getUser(id: String): Result<User> {
        return gameRemoteDataSource.getUser(id)
    }
}