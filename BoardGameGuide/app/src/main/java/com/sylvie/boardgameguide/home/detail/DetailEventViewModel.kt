package com.sylvie.boardgameguide.home.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.*
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DetailEventViewModel(private val gameRepository: GameRepository) : ViewModel() {

    var eventData = MutableLiveData<Event>()

    private var _eventData2 = MutableLiveData<Event>()

    val eventData2: LiveData<Event>
        get() = _eventData2

    private var _gameData = MutableLiveData<Game>()

    val gameData: LiveData<Game>
        get() = _gameData

    private var _allEvents = MutableLiveData<List<Event>>()

    val allEvents: LiveData<List<Event>>
        get() = _allEvents

    private var _messages = MutableLiveData<List<Event>>()

    val messages: LiveData<List<Event>>
        get() = _messages

    private var messageStatus = MutableLiveData<Boolean>()

    var imagesUri = MutableLiveData<String>()

    var localImageList = MutableLiveData<MutableList<String>>()

    private var _photoStatus = MutableLiveData<Boolean>()

    val photoStatus: LiveData<Boolean>
        get() = _photoStatus

    private var _allUsersData = MutableLiveData<List<User>>()

    val allUsersData: LiveData<List<User>>
        get() = _allUsersData

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    private var _playerStatus = MutableLiveData<Boolean>()

    val playerStatus: LiveData<Boolean>
        get() = _playerStatus

    var photoPermission = MutableLiveData<Boolean>()

    var toolNavigation = MutableLiveData<String>()

    var profileNavigation = MutableLiveData<String>()

    val joinStatus = MutableLiveData<Boolean>()

    val leaveStatus = MutableLiveData<Boolean>()

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUser()
        getAllUsers()
        getEvents()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getEvents() {
        _allEvents = gameRepository.getEvents("")
    }

    fun filterMessage(event: List<Event>, eventId: String) {
        _messages.value = event.filter { it.id == eventId }
    }

    fun setMessage(message: Message, event: Event) {
        coroutineScope.launch {
            val result = gameRepository.setMessage(message, event)
            messageStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun getAllUsers() {
        _allUsersData = gameRepository.getAllUsers()
        Log.i("event", "${_allUsersData}")
    }

    fun filterUserPhoto(hostId: String): String {
        hostId.let {
            var imageUrl: String = ""
            if (_allUsersData.value!!.any { it.id == hostId }) {
                imageUrl = _allUsersData.value!!.filter { it.id == hostId }[0].image
            }
            return imageUrl
        }
    }

    fun getUser() {
        coroutineScope.launch {
            try {
                UserManager.userToken?.let {
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

    fun getEvent(id: String) {
        coroutineScope.launch {
            val result = gameRepository.getEvent(id)
            _eventData2.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun getGame(id: String) {
        coroutineScope.launch {
            val result = gameRepository.getGame(id)
            _gameData.value = when (result) {
                is Result.Success -> {
                    if (result.data.any { it.id == id }) {
                        result.data.filter { it.id == id }[0]
                    } else {
                        null
                    }
                }
                else -> {
                    null
                }
            }
        }
    }

    fun setPlayer(userId: String, event: Event, status: Boolean) {
        coroutineScope.launch {
            val result = gameRepository.setPlayer(userId, event, status)
            _playerStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun checkUserPermission(memberList: MutableList<String>) {

        memberList.let {
            photoPermission.value = it.any { id -> id == UserManager.userToken }
        }
    }

    fun addImages(imageList: List<String>): List<String> {
        var list = mutableListOf<String>()
        list.clear()
        list = imageList.toMutableList()
        list.add("")
        return list
    }

    fun toPhotoItems(list: List<String>): List<PhotoItem> {
        val items = mutableListOf<PhotoItem>()
        list.let {
            for (event in it) {
                when (event == "") {
                    true -> items.add(PhotoItem.EmptyItem(event))
                    false -> items.add(PhotoItem.FullItem(event))
                }
            }
        }
        return items
    }

    fun addPhoto(image: String, eventId: String, status: Boolean) {
        coroutineScope.launch {
            val result = gameRepository.addPhoto(image, eventId, status)
            _photoStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun convertDateToLong(date: Timestamp): Long {
        return date.toDate().time
    }

    fun changeToolIcon(tool: String): Int {
        val drawableResource = when (tool) {
            "Dice" -> R.drawable.ic_dice_white
            "Timer" -> R.drawable.ic_timer_white
            else -> R.drawable.ic_bottle_white
        }
        return drawableResource
    }

    fun navigateToProfile(userId: String) {
        profileNavigation.value = userId
    }

    fun navigated() {
        toolNavigation.value = null
        profileNavigation.value = null
    }
}