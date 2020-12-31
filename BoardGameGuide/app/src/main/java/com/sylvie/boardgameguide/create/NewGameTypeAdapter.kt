package com.sylvie.boardgameguide.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robertlevonyan.views.chip.OnSelectClickListener
import com.sylvie.boardgameguide.databinding.ItemGameTypeBinding

class NewGameTypeAdapter(var viewModel: NewGameViewModel) :
    ListAdapter<String, NewGameTypeAdapter.GameTypeViewHolder>(DiffCallback) {

    class GameTypeViewHolder(private val binding: ItemGameTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String, viewModel: NewGameViewModel) {
            binding.data = data
            binding.textGameType.onSelectClickListener = OnSelectClickListener { _, selected ->
                viewModel.addType(data, selected)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameTypeViewHolder {
        return GameTypeViewHolder(
            ItemGameTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GameTypeViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}