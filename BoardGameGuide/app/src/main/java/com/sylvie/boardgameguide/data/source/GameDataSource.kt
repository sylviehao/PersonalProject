package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.*

interface GameDataSource {

    suspend fun createUser(user: User): Result<Boolean>

    suspend fun getHome(): Result<List<HomeItem>>

    fun getEvents(): MutableLiveData<List<Event>>

    suspend fun setLike(userId: String, event: Event, status: Boolean): Result<Boolean>

    suspend fun setPlayer(userId: String, event: Event, status: Boolean): Result<Boolean>

    suspend fun getGame(id : String): Result<List<Game>>

    suspend fun addGame(game: Game): Result<Boolean>

    suspend fun getAllGames(): Result<List<Game>>

    suspend fun getUser(id : String): Result<User?>

    fun getAllUsers(): MutableLiveData<List<User>>

    suspend fun setUser(user: User, introduction: String): Result<User>

    suspend fun setGame(user: User, game: Game): Result<Boolean>

    suspend fun removeGame(user: User, game: Game): Result<Boolean>

    suspend fun addEvent(event: Event): Result<Boolean>

}