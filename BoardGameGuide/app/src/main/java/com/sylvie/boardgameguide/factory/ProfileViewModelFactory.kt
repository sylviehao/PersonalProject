package com.sylvie.boardgameguide.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.game.detail.GameDetailViewModel
import com.sylvie.boardgameguide.profile.ProfileViewModel
import com.sylvie.boardgameguide.profile.event.ProfileEventViewModel
import com.sylvie.boardgameguide.profile.post.ProfilePostViewModel

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val gameRepository: GameRepository,
    private val Id: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(gameRepository, Id) as T
        }

        if (modelClass.isAssignableFrom(ProfilePostViewModel::class.java)) {
            return ProfilePostViewModel(gameRepository, Id) as T
        }

        if (modelClass.isAssignableFrom(ProfileEventViewModel::class.java)) {
            return ProfileEventViewModel(gameRepository, Id) as T
        }

        if (modelClass.isAssignableFrom(GameDetailViewModel::class.java)) {
            return GameDetailViewModel(gameRepository, Id) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}