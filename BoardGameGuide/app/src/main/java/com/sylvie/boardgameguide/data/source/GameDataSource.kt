package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.*

interface GameDataSource {

    suspend fun getHome(): Result<List<HomeItem>>

    fun getEvents(): MutableLiveData<List<Event>>

    suspend fun getGame(id : String): Result<List<Game>>

    suspend fun getUser(id : String): Result<User>

    suspend fun setUser(user: User, introduction: String): Result<User>

    suspend fun setGame(user: User, game: Game): Result<Boolean>

    suspend fun removeGame(user: User, game: Game): Result<Boolean>

}