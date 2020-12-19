package com.sylvie.boardgameguide.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.profile.ProfileViewModel
import com.sylvie.boardgameguide.profile.post.ProfilePostViewModel

@Suppress("UNCHECKED_CAST")
class ProfilePostViewModelFactory(
    private val gameRepository: GameRepository,
    private val userId: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProfilePostViewModel::class.java)) {
            return ProfilePostViewModel(gameRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}