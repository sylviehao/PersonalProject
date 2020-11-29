package com.sylvie.boardgameguide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.util.CurrentFragmentType

class MainViewModel : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()
}