package com.sylvie.boardgameguide.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.ItemGameBinding

class GameAdapter(private val onClickListener: OnClickListener, var viewModel: GameViewModel) :
    ListAdapter<Game, GameAdapter.GameViewHolder>(GameAdapter.DiffCallback) {

    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }

    class GameViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, onClickListener: GameAdapter.OnClickListener, viewModel: GameViewModel) {
            binding.game = game
            binding.imageGame.setOnClickListener { onClickListener.onClick(game) }

            viewModel.getUserData.let {
                if(it.value?.favorite!!.any { favorite -> favorite.id == game.id }){
                    binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                    binding.iconPin.tag = "select"
                } else {
                    binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin)
                    binding.iconPin.tag = "empty"
                }
            }

            binding.iconPin.setOnClickListener {
                if(it.tag == "empty"){
                    it.tag = "select"
                    viewModel.add2Favorite(game)
                    viewModel.boomImage(binding.imageGame)
                    it.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                } else {
                    it.tag = "empty"
                    it.setBackgroundResource(R.drawable.ic_nav_pin)
                    viewModel.removeFavorite(game)
                }
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