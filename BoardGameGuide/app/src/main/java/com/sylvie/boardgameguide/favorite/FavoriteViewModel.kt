package com.sylvie.boardgameguide.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class FavoriteViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val _detailNavigation = MutableLiveData<Game>()

    val detailNavigation: LiveData<Game>
        get() = _detailNavigation

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUser()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getUser() {

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
                _userData.value?.let { gameRepository.setGame(it, game) }
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

    fun navigateToDetail(game: Game) {
        _detailNavigation.value = game
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }
}