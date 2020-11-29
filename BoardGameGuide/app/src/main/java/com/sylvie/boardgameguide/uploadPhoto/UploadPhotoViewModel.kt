package com.sylvie.boardgameguide.uploadPhoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UploadPhotoViewModel : ViewModel() {

    // Handle leave upload
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }
}