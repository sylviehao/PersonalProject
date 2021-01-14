package com.sylvie.boardgameguide.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.data.source.GameRepository
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

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    var firebaseUser = MutableLiveData<FirebaseUser>()

    init {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun createUser() {
        coroutineScope.launch {
            firebaseUser.value?.let {
                val user = when (userData.value?.id == it.uid) {
                    true -> userData.value
                    else -> setUpNewUser(it)
                }
                user?.let { data ->
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

    private fun setUpNewUser(it: FirebaseUser): User {
        return User(
            id = it.uid,
            name = it.displayName.toString(),
            image = it.photoUrl.toString(),
            favorite = mutableListOf(),
            browseRecently = mutableListOf()
        )
    }

    fun getUser(uid: String) {
        coroutineScope.launch {
            try {
                val result = gameRepository.getUser(uid)
                _userData.value = when (result) {
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