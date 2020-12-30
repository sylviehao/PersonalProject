package com.sylvie.boardgameguide.profile.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.source.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ProfileEventViewModel(
    private val gameRepository: GameRepository,
    private val userId: String?
) : ViewModel() {

    private val _detailNavigation = MutableLiveData<Event>()

    val detailNavigation: LiveData<Event>
        get() = _detailNavigation

    private var _allEventsData = MutableLiveData<List<Event>>()

    val allEventsData: LiveData<List<Event>>
        get() = _allEventsData

    private var _myEventData = MutableLiveData<List<Event>>()

    val myEventData: LiveData<List<Event>>
        get() = _myEventData

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getAllEvents()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getAllEvents() {
        _allEventsData = gameRepository.getEvents("OPEN")
    }

    fun filterMyEvent(list: List<Event>) {
        _myEventData.value = list.filter { it.user?.id == userId }
    }

    fun navigateToDetail(event: Event) {
        _detailNavigation.value = event
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }
}