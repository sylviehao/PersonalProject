package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.LiveData
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result

interface GameRepository {

    suspend fun getEvents(id : String): Result<List<Event>>

    suspend fun getGame(id : String): Result<List<Game>>


}