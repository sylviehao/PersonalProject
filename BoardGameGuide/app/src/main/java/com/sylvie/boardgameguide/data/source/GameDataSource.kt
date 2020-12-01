package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result

interface GameDataSource {

    fun getEvents(): MutableLiveData<List<Event>>

    suspend fun getGame(id : String): Result<List<Game>>
}