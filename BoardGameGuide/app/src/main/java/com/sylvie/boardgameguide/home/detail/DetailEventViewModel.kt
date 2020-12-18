package com.sylvie.boardgameguide.home.detail

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

class DetailEventViewModel(private val gameRepository: GameRepository) : ViewModel() {
    // Save change from Event
    var getEventData = MutableLiveData<Event>()

    // Save change from Game
    var _getGameData = MutableLiveData<Game>()

    val getGameData: LiveData<Game>
        get() = _getGameData

    private var _getAllUsers = MutableLiveData<List<User>>()

    val getAllUsers: LiveData<List<User>>
        get() = _getAllUsers

    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    var _addPlayer = MutableLiveData<Boolean>()

    val addPlayer: LiveData<Boolean>
        get() = _addPlayer

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

    }

    fun getAllUsers() {
        _getAllUsers = gameRepository.getAllUsers()
        Log.i("event","${_getAllUsers}")
    }

//    fun getUser(uid: String) {
//        coroutineScope.launch {
//            try {
//                val result = gameRepository.getUser(uid)
//                _getUserData.value = when (result) {
//                    is Result.Success -> {
//                        result.data
//                    }
//                    else -> {
//                        null
//                    }
//                }
//            } catch (e: Exception) {
//            }
//        }
//    }

    fun getGame(id: String) {

        coroutineScope.launch {

            val result = gameRepository.getGame(id)
            _getGameData.value = when (result) {
                is Result.Success -> {
                    if (result.data.any { it.id == id }) {
                        result.data.filter { it.id == id }[0]
                    } else {
                        null
                    }
                } else -> {
                    null
                }
            }
        }
    }

    fun setPlayer(userId: String, event: Event, status: Boolean) {
        coroutineScope.launch {
            val result = gameRepository.setPlayer(userId, event, status)
            _addPlayer.value = when (result) {
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

    var newArray = MutableLiveData<MutableList<String>>()

    fun add(imageList: List<String>){
        var list = mutableListOf<String>()
        list.clear()
        list = imageList.toMutableList()
        list.add("")
        newArray.value = list
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

}