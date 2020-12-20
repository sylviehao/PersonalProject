package com.sylvie.boardgameguide.game.detail

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class GameDetailViewModel(private val gameRepository: GameRepository, private val gameId: String?) : ViewModel() {

    // Save change from User
    var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData

    private var _setBrowseRecentlyStatus = MutableLiveData<Boolean>()
    val setBrowseRecentlyStatus: LiveData<Boolean>
        get() = _setBrowseRecentlyStatus

    var getGameData = MutableLiveData<Game>()

    private lateinit var gameLog: BrowseRecently

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUser()
        setBrowseRecently()
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
                    val result = gameRepository.setBrowseRecently(it,gameLog)
                    _setBrowseRecentlyStatus.value = when (result) {
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
                _getUserData.value?.let { gameRepository.setGame(it,game) }
            } catch (e: Exception) {
                Log.i("Star", "${e.message}")
            }

        }
    }

    fun removeFavorite(game: Game) {
        coroutineScope.launch {
            try {
                _getUserData.value?.let { gameRepository.removeGame(it, game) }
            } catch (e: Exception) {
                Log.i("Star", "${e.message}")
            }

        }
    }

    var boomStatus = MutableLiveData<ImageView>()
    fun boomImage(view: ImageView){
        boomStatus.value = view
    }

}