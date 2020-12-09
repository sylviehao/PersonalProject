package com.sylvie.boardgameguide.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.source.GameRepository

class NewEventViewModel(private val gameRepository: GameRepository) : ViewModel() {

    val game = MutableLiveData<Game>()

    val date = MutableLiveData<Long>()

}