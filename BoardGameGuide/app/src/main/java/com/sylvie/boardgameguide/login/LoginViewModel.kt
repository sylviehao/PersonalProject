package com.sylvie.boardgameguide.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.sylvie.boardgameguide.data.BrowseRecently
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(private val gameRepository: GameRepository) : ViewModel() {


    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<Boolean>()

    val status: LiveData<Boolean>
        get() = _status

    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    var firebaseUser = MutableLiveData<FirebaseUser>()


    init {

    }

    fun createUser() {

        coroutineScope.launch {
            firebaseUser.value?.let {
                val newUser = User(
                    id = it.uid,
                    name = it.displayName.toString(),
                    image = it.photoUrl.toString(),
                    favorite = mutableListOf(Game()),
                    browseRecently = mutableListOf(BrowseRecently())
                )

                val user = when (getUserData.value?.id == it.uid) {
                    true -> getUserData.value
                    else -> newUser
                }
                user?.let {data->
                    val result = gameRepository.createUser(data)
                    _status.value = when (result) {
                        is Result.Success -> {
                            result.data
                        }
                        else -> {
                            null
                        }
                    }
                }
            }
        }
    }

    fun getUser(uid: String) {
        coroutineScope.launch {
            try {
                val result = gameRepository.getUser(uid)
                _getUserData.value = when (result) {
                    is Result.Success -> {
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

}