package com.sylvie.boardgameguide.profile.post

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.HomeItem
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ProfilePostViewModel(private val gameRepository: GameRepository, private val userId: String?) : ViewModel() {

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    // Save change from Event
    private var _getEventData = MutableLiveData<List<Event>>()

    val getEventData: LiveData<List<Event>>
        get() = _getEventData

    private var _myPostData = MutableLiveData<List<Event>>()

    val myPostData: LiveData<List<Event>>
        get() = _myPostData


    private var _getHome = MutableLiveData<List<HomeItem>>()

    val getHome: LiveData<List<HomeItem>>
        get() = _getHome


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getAllPost()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getAllPost() {
        _getEventData = gameRepository.getEvents("CLOSE")
    }

    fun filterMyPost(list: List<Event>){
        _myPostData.value = list.filter { it.user?.id == userId }
    }

    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

}