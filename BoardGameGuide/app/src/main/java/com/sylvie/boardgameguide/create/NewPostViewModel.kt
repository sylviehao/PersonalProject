package com.sylvie.boardgameguide.create

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

    private var _allUsersData = MutableLiveData<List<User>>()

    val allUsersData: LiveData<List<User>>
        get() = _allUsersData

    private var _eventStatus = MutableLiveData<Boolean>()

    val eventStatus: LiveData<Boolean>
        get() = _eventStatus

    var imagesUri = MutableLiveData<MutableList<String>>()

    var localImageList = MutableLiveData<MutableList<String>>()

    val typeList = MutableLiveData<MutableList<String>>()

    var focusStatus = MutableLiveData<Boolean>()

    var visibilityStatus = MutableLiveData<Boolean>()

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
        getAllUsers()
    }

    private fun getAllUsers() {
        _allUsersData = gameRepository.getAllUsers()
    }

    fun addPost(
        gameId: String,
        topic: String,
        description: String,
        location: String,
        rules: String,
        member: MutableList<String>,
        name: String,
        imagesUri: MutableList<String>,
        tools: MutableList<String>
    ) {
        coroutineScope.launch {
            try {
                UserManager.user.value?.let {
                    member.add(it.id)
                    val event = Event(
                        user = it,
                        topic = topic,
                        description = description,
                        image = imagesUri,
                        location = location,
                        game = Game(
                            id = gameId,
                            name = name,
                            image = mutableListOf(),
                            type = typeList.value,
                            rules = rules,
                            roles = mutableListOf(),
                            tools = tools
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

    fun filter(list: List<User>, query: String): List<User> {
        val lowerCaseQueryString = query.toLowerCase(Locale.ROOT)
        val filteredList = mutableListOf<User>()
        for (user in list) {
            val name = user.name.toLowerCase(Locale.ROOT)
            if (name.contains(lowerCaseQueryString)) {
                filteredList.add(user)
            }
        }
        return filteredList
    }

    fun editPlayer(userId: String, status: Boolean) {
        val oldPlayerList = userList.value
        if (status) {
            oldPlayerList?.add(userId)
        } else {
            oldPlayerList?.remove(userId)
        }
        focusStatus.value = true
        userList.value = oldPlayerList
    }

    fun idToName(userId: String): String {
        var name: String = ""
        _allUsersData.value?.let {
            if (it.any { user -> user.id == userId }) {
                name = it.filter { user -> user.id == userId }[0].name
            }
        }
        return name
    }

    fun addType(type: String, Status: Boolean) {
        var list = mutableListOf<String>()

        typeList.value?.let {
            list = it.toMutableList()
        }
        if (Status) {
            list.add(type)
        } else {
            list.remove(type)
        }
        typeList.value = list
    }

}