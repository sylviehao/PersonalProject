package com.sylvie.boardgameguide.game

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.ItemGameBinding
import com.sylvie.boardgameguide.home.HomeAdapter

class GameAdapter(private val onClickListener: OnClickListener, var viewModel: GameViewModel) :
    ListAdapter<Game, GameAdapter.GameViewHolder>(GameAdapter.DiffCallback) {

    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }

    class GameViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, onClickListener: GameAdapter.OnClickListener, viewModel: GameViewModel) {
            binding.game = game
            binding.textGameName.text = game.name
            binding.textGameType.text = game.type.toString()
            binding.imageGame.setOnClickListener { onClickListener.onClick(game) }
            binding.iconPin.setOnClickListener {
                if(it.tag == "empty"){
                    it.tag = "select"
                    viewModel.boomImage(binding.imageGame)
                    it.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                    viewModel.addRemoveHopeList(game)
                }else{
                    it.tag = "empty"
                    it.setBackgroundResource(R.drawable.ic_nav_pin)
                    viewModel.addRemoveHopeList(game)
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