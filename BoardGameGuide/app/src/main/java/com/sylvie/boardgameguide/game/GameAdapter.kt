package com.sylvie.boardgameguide.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.ItemGameBinding

class GameAdapter :
    ListAdapter<Game, GameAdapter.GameViewHolder>(GameAdapter.DiffCallback) {

    class GameViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.game = game
            binding.textGameName.text = game.name
            binding.textGameType.text = game.type.toString()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
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