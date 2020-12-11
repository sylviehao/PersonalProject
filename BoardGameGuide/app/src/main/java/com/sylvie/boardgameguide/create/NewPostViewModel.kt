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
        type: MutableList<String>
    ) {
        coroutineScope.launch {
            try {

                UserManager.user.value?.let {

                    member.add(it.name)
                    val event = Event(
                        user = it,
                        topic = topic,
                        description = "",
                        image = mutableListOf(
                            "https://images.unsplash.com/photo-1572979836320-ae5be0d63ab8?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MzB8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"
                        ),
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