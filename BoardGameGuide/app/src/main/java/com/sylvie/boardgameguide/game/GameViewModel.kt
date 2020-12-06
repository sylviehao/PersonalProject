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

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Game>()

    val navigateToDetail: LiveData<Game>
        get() = _navigateToDetail

    // Save change from User
    var _getUserData = MutableLiveData<User>()

    val getUserData: LiveData<User>
        get() = _getUserData



    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getUser("001")
    }

    fun getUser(id: String) {
        coroutineScope.launch {
            try {
//                UserManager.userToken?.let {
                    val result = gameRepository.getUser(id)
                    _getUserData.value = when (result) {
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

    fun add2Favorite(game: Game) {
        coroutineScope.launch {
            try {
                _getUserData.value?.let { gameRepository.setGame(it,game) }
            } catch (e: Exception) {
                Log.i("Star", "${e.message}")
            }

        }
    }

    fun navigateToDetail(game: Game) {
        _navigateToDetail.value = game
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    var boomStatus = MutableLiveData<ImageView>()
    fun boomImage(view: ImageView){
        boomStatus.value = view
    }
}