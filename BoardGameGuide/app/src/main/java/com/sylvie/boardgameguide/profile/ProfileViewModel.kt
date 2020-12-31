package com.sylvie.boardgameguide.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(private val gameRepository: GameRepository, private val userId: String?) :
    ViewModel() {

    private val _detailNavigation = MutableLiveData<Game>()

    val detailNavigation: LiveData<Game>
        get() = _detailNavigation

    private var _allEventsData = MutableLiveData<List<Event>>()

    val allEventsData: LiveData<List<Event>>
        get() = _allEventsData

    private var _myEventData = MutableLiveData<List<Event>>()

    val myEventData: LiveData<List<Event>>
        get() = _myEventData

    private var _myEventOpen = MutableLiveData<List<Event>>()

    val myEventOpen: LiveData<List<Event>>
        get() = _myEventOpen

    private var _myEventClose = MutableLiveData<List<Event>>()

    val myEventClose: LiveData<List<Event>>
        get() = _myEventClose

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    private var _setUserData = MutableLiveData<User>()

    val setUserData: LiveData<User>
        get() = _setUserData

    private var _browseRecentlyInfo = MutableLiveData<List<Game>>()

    val browseRecentlyInfo: LiveData<List<Game>>
        get() = _browseRecentlyInfo

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getEvents()
        getUser()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getEvents() {
        _allEventsData = gameRepository.getEvents("")
    }

    fun filterMyEvent(list: List<Event>) {
        _myEventData.value = list.filter { it.user?.id == userId }
    }

    fun filterMyEventStatus(list: List<Event>) {
        _myEventOpen.value = list.filter { it.status == "OPEN" }
        _myEventClose.value = list.filter { it.status == "CLOSE" }
    }

    fun getUser() {
        coroutineScope.launch {
            try {
                userId?.let {
                    val result = gameRepository.getUser(it)
                    _userData.value = when (result) {
                        is Result.Success -> {
                            result.data
                        }
                        else -> {
                            null
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun setUser(user: User, introduction: String) {
        coroutineScope.launch {
            try {
                UserManager.userToken?.let {
                    val result = gameRepository.setUser(user, introduction)
                    _setUserData.value = when (result) {
                        is Result.Success -> {
                            result.data
                        }
                        else -> {
                            null
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun getBrowseRecently(gamesId: List<String>) {
        coroutineScope.launch {
            try {
                UserManager.userToken?.let {
                    val result = gameRepository.getBrowseRecently(it, gamesId)
                    _browseRecentlyInfo.value = when (result) {
                        is Result.Success -> {
                            result.data
                        }
                        else -> {
                            null
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun navigateToDetail(game: Game) {
        _detailNavigation.value = game
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }
}