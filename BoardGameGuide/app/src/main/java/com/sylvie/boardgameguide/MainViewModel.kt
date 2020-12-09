package com.sylvie.boardgameguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.network.LoadApiStatus
import com.sylvie.boardgameguide.util.CurrentFragmentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    val navigate = MutableLiveData<Int>()

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

//    fun loginAndSetUser(userToken: String, userName: String) {
//        coroutineScope.launch {
//            _status.value = LoadApiStatus.LOADING
//            when (val result = gameRepository.login(userToken, userName)) {
//                is Result.Success<*> -> {
//                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
//                }
//                is Result.Fail -> {
//                    _error.value = result.error
//                    _status.value = LoadApiStatus.ERROR
//                }
//                is Result.Error -> {
//                    _error.value = result.exception.toString()
//                    _status.value = LoadApiStatus.ERROR
//                }
//                else -> {
//                    _error.value = GameApplication.appContext?.getString(R.string.something_wrong)
//                    _status.value = LoadApiStatus.ERROR
//                }
//            }
//        }
//    }
}