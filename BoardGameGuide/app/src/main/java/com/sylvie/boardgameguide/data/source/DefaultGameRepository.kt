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

    override suspend fun createUser(user: User): Result<Boolean> {
        return gameRemoteDataSource.createUser(user)
    }

    override suspend fun getHome(): Result<List<HomeItem>> {
        return gameRemoteDataSource.getHome()
    }

    override  fun getEvents(): MutableLiveData<List<Event>> {
        return gameRemoteDataSource.getEvents()
    }

    override suspend fun setLike(userId: String, event: Event, status: Boolean): Result<Boolean> {
        return gameRemoteDataSource.setLike(userId, event, status)
    }

    override suspend fun setPlayer(userId: String, event: Event, status: Boolean): Result<Boolean> {
        return gameRemoteDataSource.setPlayer(userId, event, status)
    }

    override suspend fun getGame(id: String): Result<List<Game>> {
        return gameRemoteDataSource.getGame(id)
    }

    override suspend fun getAllGames(): Result<List<Game>> {
        return gameRemoteDataSource.getAllGames()
    }

    override suspend fun getUser(id: String): Result<User> {
        return gameRemoteDataSource.getUser(id)
    }

    override suspend fun setUser(user: User, introduction: String): Result<User> {
        return gameRemoteDataSource.setUser(user, introduction)
    }

    override suspend fun setGame(user: User, game: Game): Result<Boolean> {
        return gameRemoteDataSource.setGame(user, game)
    }

    override suspend fun removeGame(user: User, game: Game): Result<Boolean> {
        return gameRemoteDataSource.removeGame(user, game)
    }
}