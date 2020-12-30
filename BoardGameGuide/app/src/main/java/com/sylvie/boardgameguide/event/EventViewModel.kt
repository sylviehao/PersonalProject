package com.sylvie.boardgameguide.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager

class EventViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var _allEventsData = MutableLiveData<List<Event>>()

    val allEventsData: LiveData<List<Event>>
        get() = _allEventsData

    private var _myEventsData = MutableLiveData<List<Event>>()

    val myEventsData: LiveData<List<Event>>
        get() = _myEventsData

    private val _detailNavigation = MutableLiveData<Event>()

    val detailNavigation: LiveData<Event>
        get() = _detailNavigation

    init {
        getAllEvents()
    }

    private fun getAllEvents() {
        _allEventsData = gameRepository.getEvents("OPEN")
    }

    fun filterMyEvent(list: List<Event>){
        _myEventsData.value = list
            .filter { event -> event.playerList!!
            .any { id -> id == UserManager.user.value?.id } }
    }

    fun navigateToDetail(event: Event) {
        _detailNavigation.value = event
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }
}