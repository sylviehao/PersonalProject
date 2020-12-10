package com.sylvie.boardgameguide.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class LoginViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val _status = MutableLiveData<Boolean>()

    val status: LiveData<Boolean>
        get() = _status

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {

    }

    fun createUser(googleUid: String, name: String, photo: String){
        coroutineScope.launch {
            val user = User(
                id = googleUid,
                name = name,
                image = photo,
                introduction = "",
                favorite = mutableListOf(Game()),
                browseRecently = mutableListOf(BrowseRecently())
            )
            val result = gameRepository.createUser(user)
            _status.value = when(result){
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