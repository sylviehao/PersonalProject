package com.sylvie.boardgameguide.game.detail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.source.GameRepository

class GameDetailViewModel(private val gameRepository: GameRepository) : ViewModel() {


    var getGameData = MutableLiveData<Game>()

}