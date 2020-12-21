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

    var imagesUri = MutableLiveData<MutableList<String>>()

    var localImageList = MutableLiveData<MutableList<String>>()

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
        description: String,
        location: String,
        rules: String,
//        member: MutableList<String>,
        name: String,
//        type: MutableList<String>,
        limit: String,
        imagesUri: MutableList<String>
    ) {
        coroutineScope.launch {
            try {

                UserManager.user.value?.let {
                    var playerLimit = "0"

                    playerLimit = if (limit == "") {
                        "0"
                    }else{
                        limit
                    }

                    val member = mutableListOf<String>()
                    member.add(it.id)
                    val event = Event(
                        user = it,
                        topic = topic,
                        description = description,
                        image = imagesUri,
                        time = date.value!!,
                        location = location,
                        game = Game(
                            name = name,
                            image = mutableListOf(),
                            type = typeList.value,
                            rules = rules,
                            roles = mutableListOf()
                        ),
                        message = mutableListOf(),
                        playerList = member,
                        playerLimit = playerLimit.toInt(),
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

    private val typeList = MutableLiveData<MutableList<String>>()

    fun addType(type: String, Status: Boolean){
        var list = mutableListOf<String>()

        typeList.value?.let {
            list = it.toMutableList()
        }
        if(Status){
            list.add(type)
        } else{
            list.remove(type)
        }
        typeList.value = list
    }

}