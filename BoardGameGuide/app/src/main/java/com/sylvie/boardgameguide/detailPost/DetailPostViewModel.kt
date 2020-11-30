package com.sylvie.boardgameguide.detailPost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game

class DetailPostViewModel() : ViewModel() {

    // Save change from Game
    var getGameData = MutableLiveData<Game>()


    // Save change from Event
    var getEventData = MutableLiveData<Event>()

//    val getEventData: LiveData<Event>
//        get() = _getEventData

    init {

    }
    fun getGame(){

    }
}