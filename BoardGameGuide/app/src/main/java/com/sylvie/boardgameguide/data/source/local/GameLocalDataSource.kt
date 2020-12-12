package com.sylvie.boardgameguide.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.*
import com.sylvie.boardgameguide.data.source.GameDataSource

class GameLocalDataSource(val context: Context) : GameDataSource {

    override suspend fun createUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getHome(): Result<List<HomeItem>> {
        TODO("Not yet implemented")
    }

    override fun getEvents(): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun setLike(userId: String, event: Event, status: Boolean): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setPlayer(userId: String, event: Event, status: Boolean): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getGame(id: String): Result<List<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun addGame(game: Game): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGames(): Result<List<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String): Result<User?> {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): MutableLiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun setUser(user: User, introduction: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun setGame(user: User, game: Game): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeGame(user: User, game: Game): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addEvent(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }
}