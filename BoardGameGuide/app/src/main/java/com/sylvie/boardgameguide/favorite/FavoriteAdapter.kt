package com.sylvie.boardgameguide.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.ItemFavoriteBinding
import com.sylvie.boardgameguide.databinding.ItemGameBinding
import com.sylvie.boardgameguide.game.GameAdapter

class FavoriteAdapter(private val onClickListener: FavoriteAdapter.OnClickListener) :
    ListAdapter<Game, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (favorite: Game) -> Unit) {
        fun onClick(favorite: Game) = clickListener(favorite)
    }

    class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, onClickListener: FavoriteAdapter.OnClickListener) {
            binding.game = game
            binding.textGameName.text = game.name
            binding.textGameType.text = game.type.toString()
            binding.imageGame.setOnClickListener { onClickListener.onClick(game) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game, onClickListener)
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