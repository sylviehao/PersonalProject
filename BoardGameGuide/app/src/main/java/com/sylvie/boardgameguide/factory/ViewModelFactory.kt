package com.sylvie.boardgameguide.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.create.NewEventViewModel
import com.sylvie.boardgameguide.create.NewGameViewModel
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.home.detail.DetailEventViewModel
import com.sylvie.boardgameguide.home.detail.DetailPostViewModel
import com.sylvie.boardgameguide.event.EventViewModel
import com.sylvie.boardgameguide.favorite.FavoriteViewModel
import com.sylvie.boardgameguide.game.GameViewModel
import com.sylvie.boardgameguide.home.HomeViewModel
import com.sylvie.boardgameguide.create.NewPostViewModel
import com.sylvie.boardgameguide.profile.ProfileEditViewModel
import com.sylvie.boardgameguide.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val gameRepository: GameRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(gameRepository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(gameRepository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(gameRepository)

                isAssignableFrom(NewPostViewModel::class.java) ->
                    NewPostViewModel(gameRepository)

                isAssignableFrom(NewEventViewModel::class.java) ->
                    NewEventViewModel(gameRepository)

                isAssignableFrom(NewGameViewModel::class.java) ->
                    NewGameViewModel(gameRepository)

                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel(gameRepository)

                isAssignableFrom(FavoriteViewModel::class.java) ->
                    FavoriteViewModel(gameRepository)

                isAssignableFrom(DetailPostViewModel::class.java) ->
                    DetailPostViewModel(gameRepository)

                isAssignableFrom(EventViewModel::class.java) ->
                    EventViewModel(gameRepository)

                isAssignableFrom(DetailEventViewModel::class.java) ->
                    DetailEventViewModel(gameRepository)

                isAssignableFrom(ProfileEditViewModel::class.java) ->
                    ProfileEditViewModel(gameRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}