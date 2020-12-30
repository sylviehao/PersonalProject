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
import java.util.*

class HomeViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val _detailNavigation = MutableLiveData<Event>()

    val detailNavigation: LiveData<Event>
        get() = _detailNavigation

    private var _allEventsData = MutableLiveData<List<Event>>()

    val allEventsData: LiveData<List<Event>>
        get() = _allEventsData

    private var _homeData = MutableLiveData<List<HomeItem>>()

    val homeData: LiveData<List<HomeItem>>
        get() = _homeData

    private var _allGamesData = MutableLiveData<List<Game>>()

    val allGamesData: LiveData<List<Game>>
        get() = _allGamesData

    private var viewModelJob = Job()

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
            _allEventsData = gameRepository.getEvents("")
            Log.i("event","${_allEventsData}")
    }

    fun getHome() {
        coroutineScope.launch {
            val result = gameRepository.getHome()
            _homeData.value = when (result) {
                is Result.Success -> {
                   result.data
                } else -> {
                    null
                }
            }
        }
    }

    private fun getAllGames(){
        coroutineScope.launch {
            val result = gameRepository.getAllGames()
            _allGamesData.value = when (result) {
                is Result.Success -> {
                    result.data
                } else -> {
                    null
                }
            }
        }
    }

    fun filter(list: List<Event>, query: String): List<HomeItem> {

        val lowerCaseQueryString = query.toLowerCase(Locale.ROOT)
        val filteredList = mutableListOf<HomeItem>()

        for (event in list) {
            val user = event.user!!.name.toLowerCase(Locale.ROOT)
            val topic = event.topic.toLowerCase(Locale.ROOT)
            val game = event.game!!.name.toLowerCase(Locale.ROOT)
            val location = event.location.toLowerCase(Locale.ROOT)

            if (user.contains(lowerCaseQueryString) || topic.contains(lowerCaseQueryString)
                || game.contains(lowerCaseQueryString) || location.contains(lowerCaseQueryString)) {
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
        _detailNavigation.value = event
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }
}