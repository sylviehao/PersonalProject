package com.sylvie.boardgameguide.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onClickListener: OnClickListener,
    var viewModel: FavoriteViewModel
) :
    ListAdapter<Game, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (favorite: Game) -> Unit) {
        fun onClick(favorite: Game) = clickListener(favorite)
    }

    class FavoriteViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, onClickListener: OnClickListener, viewModel: FavoriteViewModel) {
            binding.game = game

            initFavorite(viewModel.userData, game)
            binding.imageGame.setOnClickListener { onClickListener.onClick(game) }
            binding.iconPin.setOnClickListener {
                changeFavoriteStatus(it, viewModel, game)
            }

            binding.executePendingBindings()
        }

        private fun changeFavoriteStatus(
            it: View,
            viewModel: FavoriteViewModel,
            game: Game
        ) {
            if (it.tag == "select") {
                it.tag = "empty"
                it.setBackgroundResource(R.drawable.ic_nav_pin)
                viewModel.removeFavorite(game)
            } else {
                it.tag = "select"
                it.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                viewModel.add2Favorite(game)
            }
        }

        private fun initFavorite(
            it: LiveData<User>,
            game: Game
        ) {
            if (it.value?.favorite!!.any { favorite -> favorite.id == game.id }) {
                binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                binding.iconPin.tag = "select"
            } else {
                binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin)
                binding.iconPin.tag = "empty"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game, onClickListener, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }
}