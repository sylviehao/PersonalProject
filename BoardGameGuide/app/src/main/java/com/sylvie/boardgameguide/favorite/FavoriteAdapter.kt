package com.sylvie.boardgameguide.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.ItemFavoriteBinding
import com.sylvie.boardgameguide.databinding.ItemGameBinding

class FavoriteAdapter :
    ListAdapter<Game, FavoriteAdapter.FavoriteViewHolder>(FavoriteAdapter.DiffCallback) {

    class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: Game) {
            binding.favorite = favorite
            binding.textGameName.text = favorite.name
            binding.textGameType.text = favorite.type.toString()
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
        holder.bind(game)
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