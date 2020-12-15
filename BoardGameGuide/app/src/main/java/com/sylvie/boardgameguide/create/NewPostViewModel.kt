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

    var imagesUri = MutableLiveData<MutableList<String>>()

    var userList = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
//        getUser()
    }

//    fun getUser() {
//        coroutineScope.launch {
//            try {
//                UserManager.userToken?.let {
//                    val result = gameRepository.getUser(it)
//                    _getUserData.value = when (result) {
//                        is Result.Success -> {
//                            result.data
//                        }
//                        else -> {
//                            null
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//            }
//        }
//    }

    fun addPost(
        topic: String,
        location: String,
        rules: String,
        member: MutableList<String>,
        name: String,
        type: MutableList<String>,
        imagesUri: MutableList<String>
    ) {
        coroutineScope.launch {
            try {

                UserManager.user.value?.let {

                    val event = Event(
                        user = it,
                        topic = topic,
                        description = "",
                        image = imagesUri,
                        time = date.value!!,
                        location = location,
                        game = Game(
                            name = name,
                            image = mutableListOf(),
                            type = type,
                            rules = rules,
                            roles = mutableListOf()
                        ),
                        message = mutableListOf(),
                        playerList = member,
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
                }
            } catch (e: Exception) {
            }

        }
    }

}