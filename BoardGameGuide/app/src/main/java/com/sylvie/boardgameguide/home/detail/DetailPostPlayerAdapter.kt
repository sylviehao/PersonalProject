package com.sylvie.boardgameguide.home.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.databinding.ItemChipsBinding

class DetailPostPlayerAdapter(val viewModel: DetailPostViewModel) :
    ListAdapter<String, DetailPostPlayerAdapter.PlayerViewHolder>(DiffCallback) {

    class PlayerViewHolder(private val binding: ItemChipsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String, viewModel: DetailPostViewModel) {
            binding.data = data

            initChipStatus(viewModel, data)
            binding.textPlayer.setOnClickListener {
                viewModel.playerNavigation.value = data
            }

            binding.executePendingBindings()
        }

        private fun initChipStatus(
            viewModel: DetailPostViewModel,
            data: String
        ) {
            binding.textPlayer.closable = false
            if (viewModel.allUsersData.value!!.any { it.id == data }) {
                binding.textPlayer.text =
                    viewModel.allUsersData.value!!.filter { it.id == data }[0].name
            } else {
                binding.textPlayer.text = ""
            }
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