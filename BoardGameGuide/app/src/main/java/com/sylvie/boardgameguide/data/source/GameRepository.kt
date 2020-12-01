package com.sylvie.boardgameguide.data.source

import androidx.lifecycle.LiveData
import com.sylvie.boardgameguide.data.Event

interface GameRepository {

    suspend fun getEvent(): LiveData<List<Event>>


}