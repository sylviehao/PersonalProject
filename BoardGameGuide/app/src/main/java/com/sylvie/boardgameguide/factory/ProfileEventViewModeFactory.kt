package com.sylvie.boardgameguide.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.profile.event.ProfileEventViewModel
import com.sylvie.boardgameguide.profile.post.ProfilePostViewModel

@Suppress("UNCHECKED_CAST")
class ProfileEventViewModeFactory(
    private val gameRepository: GameRepository,
    private val userId: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProfileEventViewModel::class.java)) {
            return ProfileEventViewModel(gameRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}