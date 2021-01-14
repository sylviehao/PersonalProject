package com.sylvie.boardgameguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.Result
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.network.LoadApiStatus
import com.sylvie.boardgameguide.util.CurrentFragmentType
import com.sylvie.boardgameguide.util.DrawerToggleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    val currentDrawerToggleType: LiveData<DrawerToggleType> =
        Transformations.map(currentFragmentType) {
            when (it) {
                CurrentFragmentType.NEW_POST -> DrawerToggleType.BACK
                CurrentFragmentType.NEW_EVENT -> DrawerToggleType.BACK
                CurrentFragmentType.DETAIL_POST -> DrawerToggleType.BACK
                CurrentFragmentType.DETAIL_EVENT -> DrawerToggleType.BACK
                CurrentFragmentType.NEW_GAME -> DrawerToggleType.BACK
                CurrentFragmentType.DETAIL_GAME -> DrawerToggleType.BACK
                CurrentFragmentType.DICE -> DrawerToggleType.BACK
                CurrentFragmentType.TIMER -> DrawerToggleType.BACK
                CurrentFragmentType.PICKER -> DrawerToggleType.BACK
                else -> DrawerToggleType.NORMAL
            }
        }

    val navigation = MutableLiveData<Int>()

    // check user login status
    val isLoggedIn
        get() = UserManager.isLoggedIn

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _userData = MutableLiveData<User>()

    val userData: LiveData<User>
        get() = _userData


    init {
        UserManager.userToken?.let { getUser(it) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun getUser(uid: String) {
        coroutineScope.launch {
            try {
                val result = gameRepository.getUser(uid)
                UserManager.user.value = when (result) {
                    is Result.Success -> {
                        _userData.value = result.data
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}