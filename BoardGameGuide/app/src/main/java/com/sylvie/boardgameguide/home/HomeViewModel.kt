package com.sylvie.boardgameguide.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.HomeItem
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    // Save change from Event
    private var _getEventData = MutableLiveData<List<Event>>()

    val getEventData: LiveData<List<Event>>
        get() = _getEventData

    private var _getHome = MutableLiveData<List<HomeItem>>()

    val getHome: LiveData<List<HomeItem>>
        get() = _getHome

    private var _getAllGame = MutableLiveData<List<Game>>()

    val getAllGame: LiveData<List<Game>>
        get() = _getAllGame

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getEvents()
        getHome()
        getAllGames()
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getEvents() {
            _getEventData = gameRepository.getEvents()
            Log.i("event","${_getEventData}")
    }
    fun getHome() {
        coroutineScope.launch {
            val result = gameRepository.getHome()
            _getHome.value = when (result) {
                is Result.Success -> {
                   result.data
                } else -> {
                    null
                }
            }
        }
    }

    fun getAllGames(){
        coroutineScope.launch {
            val result = gameRepository.getAllGames()
            _getAllGame.value = when (result) {
                is Result.Success -> {
                    result.data
                } else -> {
                    null
                }
            }
        }
    }

    fun getGame(id: String) : String {
        val a = _getAllGame.value?.filter { it.id == id }?.get(0)
        return a!!.name
    }

    fun filter(list: List<Event>, query: String): List<HomeItem> {

        val lowerCaseQueryString = query.toLowerCase()
        val filteredList = mutableListOf<HomeItem>()

        for (event in list) {
            val host = event.hostId
            val topic = event.topic
            if (host.contains(lowerCaseQueryString) || topic.contains(lowerCaseQueryString)) {
                val homeItem = when (event.status) {
                    "OPEN" ->HomeItem.EventItem(event)
                    "CLOSE" ->HomeItem.PostItem(event)
                    else -> null
                }
                if (homeItem != null) {
                    filteredList.add(homeItem)
                }
            }
        }
        return filteredList
    }

    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}