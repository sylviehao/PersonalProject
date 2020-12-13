package com.sylvie.boardgameguide.profile

import android.util.Log
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

class ProfileViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Game>()

    val navigateToDetail: LiveData<Game>
        get() = _navigateToDetail

    // Save change from Event
    private var _getEventData = MutableLiveData<List<Event>>()

    val getEventData: LiveData<List<Event>>
        get() = _getEventData

    // Save change from User
    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    // Save change from User
    private var _setUserData = MutableLiveData<User>()

    val setUserData: LiveData<User>
        get() = _setUserData

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getEvents()
        getUser()
    }

    private fun getEvents() {
        _getEventData = gameRepository.getEvents("")
    }



    fun getUser() {
        coroutineScope.launch {
            try {
                UserManager.userToken?.let {
                val result = gameRepository.getUser(it)
                _getUserData.value = when (result) {
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

    fun navigateToDetail(game: Game) {
        _navigateToDetail.value = game
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}