package com.sylvie.boardgameguide.game

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.data.source.GameRepository
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


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


//    fun addRemoveHopeList() {
//        coroutineScope.launch {
//            try {
//                var data = HopeListStatus(
//                        data = HopeStatus (
//                            product_id = id,
//                            status = status
//                        )
//                    )
//            } catch (e: Exception) {
//                Log.i("Star", "${e.message}")
//            }
//
//        }
//    }

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