package com.sylvie.boardgameguide.home.detail

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
import java.text.SimpleDateFormat
import java.util.*

class DetailPostViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Save change from Game
    private var _getGameData = MutableLiveData<Game>()

    val getGameData: LiveData<Game>
        get() = _getGameData

    private var _like = MutableLiveData<Boolean>()

    val like: LiveData<Boolean>
        get() = _like

    private var _getAllUsers = MutableLiveData<List<User>>()

    val getAllUsers: LiveData<List<User>>
        get() = _getAllUsers

    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    // Save change from Event
    var getEventData = MutableLiveData<Event>()

    var photoPermission = MutableLiveData<Boolean>()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
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

//    fun getGame(id: String) {
//
//        coroutineScope.launch {
//
//            val result = gameRepository.getGame(id)
//            _getGameData.value = when (result) {
//                is Result.Success -> {
//                    if (result.data.any { it.id == id }) {
//                        result.data.filter { it.id == id }[0]
//                    } else {
//                        null
//                    }
//                } else -> {
//                    null
//                }
//            }
//        }
//    }

    fun setEvent(userId: String, event: Event, status: Boolean) {
        coroutineScope.launch {
            val result = gameRepository.setLike(userId, event, status)
            _like.value = when (result) {
                is Result.Success -> {
                   result.data
                } else -> {
                    null
                }
            }
        }
    }

    fun checkUserPermission(memberList: MutableList<String>){

        memberList.let {
            photoPermission.value = it.any { id-> id == UserManager.userToken  }
        }
    }

    fun getAllUsers() {
        _getAllUsers = gameRepository.getAllUsers()
    }

}