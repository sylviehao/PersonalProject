package com.sylvie.boardgameguide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.util.CurrentFragmentType

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    val navigate = MutableLiveData<Int>()
}