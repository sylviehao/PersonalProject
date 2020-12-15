package com.sylvie.boardgameguide.home.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.databinding.ItemDetailEventPlayerBinding
import com.sylvie.boardgameguide.databinding.ItemDetailPostPlayerBinding
import com.sylvie.boardgameguide.game.GameViewModel
import com.sylvie.boardgameguide.login.UserManager

class DetailEventPlayerAdapter(var viewModel: DetailEventViewModel):
    ListAdapter<String, DetailEventPlayerAdapter.PlayerViewHolder>(DiffCallback) {

    class PlayerViewHolder(private val binding: ItemDetailEventPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String, viewModel: DetailEventViewModel) {
            binding.data = data

            if(viewModel.getAllUsers.value!!.any { it.id == data }){
                binding.textPlayer.text = viewModel.getAllUsers.value!!.filter { it.id == data }[0].name
            }else{
                binding.textPlayer.text = ""
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            ItemDetailEventPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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