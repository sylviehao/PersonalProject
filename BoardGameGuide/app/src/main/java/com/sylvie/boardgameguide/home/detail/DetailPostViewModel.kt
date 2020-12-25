package com.sylvie.boardgameguide.home.detail

import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class DetailPostViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Save change from Game
    private var _getGameData = MutableLiveData<Game>()

    val getGameData: LiveData<Game>
        get() = _getGameData

    private var _getEventData2 = MutableLiveData<Event>()

    val getEventData2: LiveData<Event>
        get() = _getEventData2

    private var _like = MutableLiveData<Boolean>()

    val like: LiveData<Boolean>
        get() = _like

    private var _photoStatus = MutableLiveData<Boolean>()

    val photoStatus: LiveData<Boolean>
        get() = _photoStatus

    private var _getPhoto = MutableLiveData<List<PhotoItem>>()

    val getPhoto: LiveData<List<PhotoItem>>
        get() = _getPhoto

    private var _getAllUsers = MutableLiveData<List<User>>()

    val getAllUsers: LiveData<List<User>>
        get() = _getAllUsers


    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    var imagesUri = MutableLiveData<String>()

    var localImageList = MutableLiveData<MutableList<String>>()

    // Save change from Event
    var getEventData = MutableLiveData<Event>()

    var photoPermission = MutableLiveData<Boolean>()

    var playerNavigation = MutableLiveData<String>()

    var navigateToTool = MutableLiveData<String>()

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
        getAllUsers()
    }


    fun getEvent(id: String) {
        coroutineScope.launch {
            val result = gameRepository.getEvent(id)
            _getEventData2.value = when (result) {
                is Result.Success -> {
                    result.data
                } else -> {
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

    fun addPhoto(image: String, eventId: String, status: Boolean) {
        coroutineScope.launch {
            val result = gameRepository.addPhoto(image, eventId, status)
            _photoStatus.value = when (result) {
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

    fun filterUserPhoto(hostId: String): String{
        hostId.let {
            var imageUrl: String = ""
            if (_getAllUsers.value!!.any { it.id == hostId }) {
               imageUrl = _getAllUsers.value!!.filter { it.id == hostId }[0].image
            }
            return imageUrl
        }
    }

    fun filterPlayer(hostId: String) {

    }


    fun add(imageList: List<String>): List<String>{
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