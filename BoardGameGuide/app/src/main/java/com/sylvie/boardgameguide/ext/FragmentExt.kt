package com.sylvie.boardgameguide.ext

import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.GameApplication
import com.sylvie.boardgameguide.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as GameApplication).gameRepository
    return ViewModelFactory(repository)
}
