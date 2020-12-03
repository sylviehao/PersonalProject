package com.sylvie.boardgameguide.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.source.GameRepository

class EventViewModel(private val gameRepository: GameRepository) : ViewModel() {
    var getEventData = MutableLiveData<Event>()
}