package com.sylvie.boardgameguide.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.source.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    // Save change from Event
    private var _getEventData = MutableLiveData<List<Event>>()

    val getEventData: LiveData<List<Event>>
        get() = _getEventData

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getEvents()
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getEvents() {
            _getEventData = gameRepository.getEvents()
            Log.i("event","${_getEventData}")
    }

    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}