package com.sylvie.boardgameguide.ext

import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.GameApplication
import com.sylvie.boardgameguide.factory.ProfileEventViewModeFactory
import com.sylvie.boardgameguide.factory.ProfilePostViewModelFactory
import com.sylvie.boardgameguide.factory.ProfileViewModelFactory
import com.sylvie.boardgameguide.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as GameApplication).gameRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(userId: String): ProfileViewModelFactory {
    val repository = (requireContext().applicationContext as GameApplication).gameRepository
    return ProfileViewModelFactory(repository, userId)
}
