package com.sylvie.boardgameguide.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.sylvie.boardgameguide.data.*
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailPostViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var _allEvents = MutableLiveData<List<Event>>()

    val allEvents: LiveData<List<Event>>
        get() = _allEvents

    private var _messages = MutableLiveData<List<Event>>()

    val messages: LiveData<List<Event>>
        get() = _messages

    private var _eventData2 = MutableLiveData<Event>()

    val eventData2: LiveData<Event>
        get() = _eventData2

    private var _likeStatus = MutableLiveData<Boolean>()

    val likeStatus: LiveData<Boolean>
        get() = _likeStatus

    private var _messageStatus = MutableLiveData<Boolean>()

    val messageStatus: LiveData<Boolean>
        get() = _messageStatus

    private var _photoStatus = MutableLiveData<Boolean>()

    val photoStatus: LiveData<Boolean>
        get() = _photoStatus

    private var _allUsersData = MutableLiveData<List<User>>()

    val allUsersData: LiveData<List<User>>
        get() = _allUsersData

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    var imagesUri = MutableLiveData<String>()

    var localImageList = MutableLiveData<MutableList<String>>()

    var eventData = MutableLiveData<Event>()

    var photoPermission = MutableLiveData<Boolean>()

    var playerNavigation = MutableLiveData<String>()

    var navigateToTool = MutableLiveData<String>()

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getUser()
        getAllUsers()
        getEvents()
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
            _messageStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
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


    fun setLike(userId: String, event: Event, status: Boolean) {
        coroutineScope.launch {
            val result = gameRepository.setLike(userId, event, status)
            _likeStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
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

    fun checkUserPermission(memberList: MutableList<String>) {
        memberList.let {
            photoPermission.value = it.any { id -> id == UserManager.userToken }
        }
    }

    private fun getAllUsers() {
        _allUsersData = gameRepository.getAllUsers()
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

    fun convertDateToLong(date: Timestamp): Long {
        return date.toDate().time
    }

    fun navigated() {
        navigateToTool.value = null
        playerNavigation.value = null
    }

}