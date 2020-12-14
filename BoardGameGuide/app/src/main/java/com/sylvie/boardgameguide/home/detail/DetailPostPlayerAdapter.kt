package com.sylvie.boardgameguide.home.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.databinding.ItemChipsBinding
import com.sylvie.boardgameguide.databinding.ItemDetailPostPlayerBinding

class DetailPostPlayerAdapter:
    ListAdapter<String, DetailPostPlayerAdapter.PlayerViewHolder>(DiffCallback) {

    class PlayerViewHolder(private val binding: ItemChipsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.data = data
            binding.textPlayer.closable = false
            binding.textPlayer.text = data
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
        holder.bind(event)
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