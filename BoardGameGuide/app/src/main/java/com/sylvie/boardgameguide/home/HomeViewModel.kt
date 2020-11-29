package com.sylvie.boardgameguide.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event

class HomeViewModel : ViewModel() {

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail


    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}