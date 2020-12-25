package com.sylvie.boardgameguide.game.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.databinding.ItemToolsBinding

class GameDetailToolAdapter(var viewModel: GameDetailViewModel):
    ListAdapter<String, GameDetailToolAdapter.PlayerViewHolder>(DiffCallback) {

    class PlayerViewHolder(private val binding: ItemToolsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String, viewModel: GameDetailViewModel) {
            binding.data = data
            binding.iconTool.setImageResource(viewModel.changeToolIcon(data))
            binding.buttonTool.setOnClickListener {
                viewModel.navigateToTool.value = data
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            ItemToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}