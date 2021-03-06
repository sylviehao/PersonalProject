package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.*

interface GameRepository {

    suspend fun createUser(user: User): Result<Boolean>

    suspend fun getHome(): Result<List<HomeItem>>

    fun getEvents(status: String): MutableLiveData<List<Event>>

    suspend fun getEvent(id: String): Result<Event?>

    suspend fun setLike(userId: String, event: Event, status: Boolean): Result<Boolean>

    suspend fun setMessage(message: Message, event: Event): Result<Boolean>

    suspend fun setPlayer(userId: String, event: Event, status: Boolean): Result<Boolean>

    suspend fun addPhoto(image: String, eventId: String, status: Boolean): Result<Boolean>

    suspend fun getGame(id : String): Result<List<Game>>

    suspend fun getAllGames(): Result<List<Game>>

    suspend fun getUser(id : String): Result<User?>

    fun getAllUsers(): MutableLiveData<List<User>>

    suspend fun setUser(user: User, introduction: String): Result<User>

    suspend fun setGame(user: User, game: Game): Result<Boolean>

    suspend fun addGame(game: Game): Result<Boolean>

    suspend fun removeGame(user: User, game: Game): Result<Boolean>

    suspend fun addEvent(event: Event): Result<Boolean>

    suspend fun setBrowseRecently(userId: String,gameId: BrowseRecently): Result<Boolean>

    suspend fun getBrowseRecently(userId: String, gamesId: List<String>): Result<List<Game>>

}