package com.sylvie.boardgameguide.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailPostViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Save change from Game
    var _getGameData = MutableLiveData<Game>()

    val getGameData: LiveData<Game>
        get() = _getGameData


    // Save change from Event
    var getEventData = MutableLiveData<Event>()

//    val getEventData: LiveData<Event>
//        get() = _getEventData

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
}