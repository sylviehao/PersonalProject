package com.sylvie.boardgameguide.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.databinding.FragmentNewGameBinding
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class NewGameViewModel(private val gameRepository: GameRepository) : ViewModel() {

    val date = MutableLiveData<Long>()

    var imagesUri = MutableLiveData<MutableList<String>>()

    var test = MutableLiveData<Boolean>()

    private var _gameStatus = MutableLiveData<Boolean>()

    val gameStatus: LiveData<Boolean>
        get() = _gameStatus

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun addGame(
        name: String,
        type: MutableList<String>,
        limit: Int,
        time: Long,
        rules: String,
        roles: MutableList<String>,
        imagesUri: MutableList<String>
    ) {

        coroutineScope.launch {
            try {

//                UserManager.user.value?.let {
                    val game = Game(
                        name = name,
                        image = imagesUri,
                        type = type,
                        playerLimit = limit,
                        time = time,
                        rules = rules,
                        roles = roles

                    )

                    val result = gameRepository.addGame(game)
                    _gameStatus.value = when (result) {
                        is Result.Success -> {
                            result.data
                        }
                        else -> {
                            null
                        }
                    }
//                }
            } catch (e: Exception) {
            }

        }
    }

}