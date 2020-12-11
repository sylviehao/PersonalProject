package com.sylvie.boardgameguide.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.*
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class NewPostViewModel(private val gameRepository: GameRepository) : ViewModel() {

    val game = MutableLiveData<Game>()

    val event = MutableLiveData<Event>()

    val date = MutableLiveData<Long>()

    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    private var _eventStatus = MutableLiveData<Boolean>()

    val eventStatus: LiveData<Boolean>
        get() = _eventStatus

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getUser()
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

    fun addPost(topic: String, location: String, rules: String, member: String) {
        coroutineScope.launch {
            try {
                val event = Event(
                    hostId = getUserData.value!!.name,
                    topic = topic,
                    description = "",
                    image = mutableListOf(
                        "https://images.unsplash.com/photo-1556374002-a892c2598e99?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MjN8fGdhbWV8ZW58MHx8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
                    ),
                    time = date.value!!,
                    location = location,
                    gameId = "a004",
                    message = mutableListOf(),
                    rules = rules,
                    playerList = mutableListOf(getUserData.value!!.name, member),
                    status = "CLOSE",
                    like = mutableListOf()
                )

                val result = gameRepository.addEvent(event)
                _eventStatus.value = when (result) {
                    is Result.Success -> {
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            } catch (e: Exception) {
            }

        }
    }

}