package com.sylvie.boardgameguide.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class NewGameViewModel(private val gameRepository: GameRepository) : ViewModel() {

    val date = MutableLiveData<Long>()

    var imagesUri = MutableLiveData<MutableList<String>>()

    var localImageList = MutableLiveData<MutableList<String>>()

    val typeList = MutableLiveData<MutableList<String>>()

    private val toolList = MutableLiveData<MutableList<String>>()

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
        limit: String,
        time: String,
        rules: String,
        roles: MutableList<String>,
        imagesUri: MutableList<String>
    ) {

        coroutineScope.launch {
            try {

                var gameTime = 0L

                gameTime = if (time == "") {
                    0
                } else {
                    time.toLong()
                }

                var playerLimit = 0

                playerLimit = if (limit == "") {
                    0
                } else {
                    limit.toInt()
                }

                UserManager.user.value?.let {
                    val game = Game(
                        name = name,
                        image = imagesUri,
                        type = typeList.value,
                        playerLimit = playerLimit,
                        time = gameTime,
                        rules = rules,
                        roles = roles,
                        tools = toolList.value
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
                }
            } catch (e: Exception) {
            }

        }
    }

    fun addTool(type: String, Status: Boolean) {
        var list = mutableListOf<String>()

        toolList.value?.let {
            list = it.toMutableList()
        }
        if (Status) {
            list.add(type)
        } else {
            list.remove(type)
        }
        toolList.value = list
    }

    fun addType(type: String, Status: Boolean) {
        var list = mutableListOf<String>()

        typeList.value?.let {
            list = it.toMutableList()
        }
        if (Status) {
            list.add(type)
        } else {
            list.remove(type)
        }
        typeList.value = list
    }

}