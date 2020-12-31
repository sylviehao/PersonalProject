package com.sylvie.boardgameguide.game.detail

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.BrowseRecently
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

class GameDetailViewModel(private val gameRepository: GameRepository, private val gameId: String?) :
    ViewModel() {

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData

    private var _browseRecentlyStatus = MutableLiveData<Boolean>()

    val browseRecentlyStatus: LiveData<Boolean>
        get() = _browseRecentlyStatus

    var gameData = MutableLiveData<Game>()

    private lateinit var gameLog: BrowseRecently

    var toolsNavigation = MutableLiveData<String>()

    var boomStatus = MutableLiveData<ImageView>()

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

    fun setBrowseRecently() {
        coroutineScope.launch {
            try {
                gameId?.let {
                    gameLog = BrowseRecently(
                        gameId = gameId,
                        time = System.currentTimeMillis()
                    )
                }
                UserManager.userToken?.let {
                    val result = gameRepository.setBrowseRecently(it, gameLog)
                    _browseRecentlyStatus.value = when (result) {
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

    fun changeToolIcon(tool: String): Int {
        return when (tool) {
            "Dice" -> R.drawable.ic_dice_white
            "Timer" -> R.drawable.ic_timer_white
            else -> R.drawable.ic_bottle_white
        }
    }

    fun navigated() {
        toolsNavigation.value = null
    }

    fun boomImage(view: ImageView) {
        boomStatus.value = view
    }

}