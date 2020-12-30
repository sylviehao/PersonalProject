package com.sylvie.boardgameguide.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robertlevonyan.views.chip.OnCloseClickListener
import com.sylvie.boardgameguide.databinding.ItemChipsBinding

class NewPostPlayerAdapter(var viewModel: NewPostViewModel) :
    ListAdapter<String, NewPostPlayerAdapter.PlayerViewHolder>(DiffCallback) {

    class PlayerViewHolder(private val binding: ItemChipsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String, viewModel: NewPostViewModel) {
            binding.data = data
            binding.textPlayer.text = viewModel.idToName(data)
            binding.textPlayer.onCloseClickListener = OnCloseClickListener { v ->
                viewModel.editPlayer(data, false)
                viewModel.visibilityStatus.value = true
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            ItemChipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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