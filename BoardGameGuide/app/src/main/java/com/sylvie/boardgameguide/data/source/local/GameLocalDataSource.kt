package com.sylvie.boardgameguide.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.HomeItem
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameDataSource

class GameLocalDataSource(val context: Context) : GameDataSource {
    override suspend fun getHome(): Result<List<HomeItem>> {
        TODO("Not yet implemented")
    }

    override fun getEvents(): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGame(id: String): Result<List<Game>> {
        TODO("Not yet implemented")
    }


}