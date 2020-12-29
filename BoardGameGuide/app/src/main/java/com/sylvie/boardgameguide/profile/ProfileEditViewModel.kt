package com.sylvie.boardgameguide.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileEditViewModel(private val gameRepository: GameRepository) : ViewModel() {


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val user = MutableLiveData<User>()

    var imageUri = MutableLiveData<String>()

    private var _setUserData = MutableLiveData<User>()
    val setUserData: LiveData<User>
        get() = _setUserData


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {

    }

    fun setUser(user: User, introduction: String) {
        coroutineScope.launch {
            try {
                UserManager.userToken?.let {
                    val result = gameRepository.setUser(user, introduction)
                    _setUserData.value = when (result) {
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