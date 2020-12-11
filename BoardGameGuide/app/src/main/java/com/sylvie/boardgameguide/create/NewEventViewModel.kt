package com.sylvie.boardgameguide.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class NewEventViewModel(private val gameRepository: GameRepository) : ViewModel() {

    val game = MutableLiveData<Game>()

    val date = MutableLiveData<Long>()

    private var _eventStatus = MutableLiveData<Boolean>()

    val eventStatus: LiveData<Boolean>
        get() = _eventStatus

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun addPost(
        topic: String,
        location: String,
        rules: String,
//        member: MutableList<String>,
        name: String,
        type: MutableList<String>,
        limit: Int
    ) {
        coroutineScope.launch {
            try {

                UserManager.user.value?.let {

//                    member.add(it.name)
                    val event = Event(
                        user = it,
                        topic = topic,
                        description = "",
                        image = mutableListOf(
                            "https://images.unsplash.com/photo-1506654020181-7c2ef87cc5a9?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MjZ8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"
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
//                        playerList = member,
                        playerLimit = limit,
                        status = "OPEN",
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