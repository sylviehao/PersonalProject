package com.sylvie.boardgameguide.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.source.GameRepository

class FavoriteViewModel(private val gameRepository: GameRepository) : ViewModel() {
    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Game>()

    val navigateToDetail: LiveData<Game>
        get() = _navigateToDetail

    fun navigateToDetail(game: Game) {
        _navigateToDetail.value = game
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}