package com.sylvie.boardgameguide.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager

class EventViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var _getEventData = MutableLiveData<List<Event>>()

    val getEventData: LiveData<List<Event>>
        get() = _getEventData

    private var _myEventData = MutableLiveData<List<Event>>()

    val myEventData: LiveData<List<Event>>
        get() = _myEventData

    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail


    init {
        getAllEvents()
    }

    private fun getAllEvents() {
        _getEventData = gameRepository.getEvents("OPEN")
    }

    fun filterMyEvent(list: List<Event>){
        _myEventData.value = list
            .filter { event -> event.playerList!!
            .any { id -> id == UserManager.user.value?.id } }
    }

    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}