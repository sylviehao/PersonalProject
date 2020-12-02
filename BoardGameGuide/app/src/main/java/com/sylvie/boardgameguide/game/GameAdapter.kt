package com.sylvie.boardgameguide.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.ItemGameBinding
import com.sylvie.boardgameguide.home.HomeAdapter

class GameAdapter(private val onClickListener: GameAdapter.OnClickListener) :
    ListAdapter<Game, GameAdapter.GameViewHolder>(GameAdapter.DiffCallback) {

    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }

    class GameViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, onClickListener: GameAdapter.OnClickListener) {
            binding.game = game
            binding.textGameName.text = game.name
            binding.textGameType.text = game.type.toString()
            binding.imageGame.setOnClickListener { onClickListener.onClick(game) }
            binding.iconPin.setOnClickListener {

            }
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