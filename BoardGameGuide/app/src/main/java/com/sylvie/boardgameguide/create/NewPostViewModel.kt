package com.sylvie.boardgameguide.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.User
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

    val date = MutableLiveData<Long>()

    private var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUser()
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

}