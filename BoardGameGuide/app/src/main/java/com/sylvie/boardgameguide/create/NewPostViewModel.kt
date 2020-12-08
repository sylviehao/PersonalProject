package com.sylvie.boardgameguide.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.source.GameRepository
import java.util.*

class NewPostViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Handle leave newPost
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    val game = MutableLiveData<Game>()

    val event = MutableLiveData<Event>()

    val date = MutableLiveData<Long>()

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }
}