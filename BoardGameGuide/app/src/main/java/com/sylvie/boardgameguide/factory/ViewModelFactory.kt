package com.sylvie.boardgameguide.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.source.GameRepository
import com.sylvie.boardgameguide.detailPost.DetailPostViewModel
import com.sylvie.boardgameguide.favorite.FavoriteViewModel
import com.sylvie.boardgameguide.game.GameViewModel
import com.sylvie.boardgameguide.home.HomeViewModel
import com.sylvie.boardgameguide.newPost.NewPostViewModel
import com.sylvie.boardgameguide.uploadPhoto.UploadPhotoViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val gameRepository: GameRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(gameRepository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(gameRepository)

                isAssignableFrom(NewPostViewModel::class.java) ->
                    NewPostViewModel(gameRepository)

                isAssignableFrom(UploadPhotoViewModel::class.java) ->
                    UploadPhotoViewModel(gameRepository)

                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel(gameRepository)

                isAssignableFrom(FavoriteViewModel::class.java) ->
                    FavoriteViewModel(gameRepository)

                isAssignableFrom(DetailPostViewModel::class.java) ->
                    DetailPostViewModel(gameRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}