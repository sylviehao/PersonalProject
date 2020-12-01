package com.sylvie.boardgameguide.ext

import android.app.Activity
import com.sylvie.boardgameguide.GameApplication
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as GameApplication).gameRepository
    return ViewModelFactory(repository)
}