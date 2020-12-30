package com.sylvie.boardgameguide.profile.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.HomeItem
import com.sylvie.boardgameguide.data.source.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ProfilePostViewModel(
    private val gameRepository: GameRepository,
    private val userId: String?
) : ViewModel() {

    private val _detailNavigation = MutableLiveData<Event>()

    val detailNavigation: LiveData<Event>
        get() = _detailNavigation

    private var _allEventsData = MutableLiveData<List<Event>>()

    val allEventsData: LiveData<List<Event>>
        get() = _allEventsData

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
        _allEventsData = gameRepository.getEvents("CLOSE")
    }

    fun filterMyPost(list: List<Event>) {
        _myPostData.value = list.filter { it.user?.id == userId }
    }

    fun navigateToDetail(event: Event) {
        _detailNavigation.value = event
    }

    fun onDetailNavigated() {
        _detailNavigation.value = null
    }
}