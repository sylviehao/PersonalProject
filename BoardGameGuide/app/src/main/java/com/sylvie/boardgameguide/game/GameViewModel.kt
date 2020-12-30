package com.sylvie.boardgameguide.game

import android.util.Log
import android.widget.ImageView
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


class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val _detailNavigation = MutableLiveData<Game>()

    val detailNavigation: LiveData<Game>
        get() = _detailNavigation

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    private var _gameData = MutableLiveData<List<Game>>()

    val gameData: LiveData<List<Game>>
        get() = _gameData

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUser()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getUser() {
        coroutineScope.launch {
            try {
                UserManager.userToken?.let {
                    val result = gameRepository.getUser(it)
                    _userData.value = when (result) {
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

    fun add2Favorite(game: Game) {
        coroutineScope.launch {
            try {
                _userData.value?.let { gameRepository.setGame(it,game) }
            } catch (e: Exception) {
                Log.i("Star", "${e.message}")
            }
        }
    }

    fun removeFavorite(game: Game) {
        coroutineScope.launch {
            try {
                _userData.value?.let { gameRepository.removeGame(it, game) }
            } catch (e: Exception) {
                Log.i("Star", "${e.message}")
            }
        }
    }

    fun getAllGames() {

        coroutineScope.launch {
            val result = gameRepository.getAllGames()
            _gameData.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> null
            }
        }
    }

    fun filter(list: List<Game>, query: String): List<Game> {
        val lowerCaseQueryString = query.toLowerCase()
        val filteredList = mutableListOf<Game>()
        for (game in list) {
            val name = game.name
            if (name.contains(lowerCaseQueryString)) {
                    filteredList.add(game)
                }
            }
        return filteredList
    }

    fun navigateToDetail(game: Game) {
        _detailNavigation.value = game
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }

    var boomStatus = MutableLiveData<ImageView>()
    fun boomImage(view: ImageView){
        boomStatus.value = view
    }
}