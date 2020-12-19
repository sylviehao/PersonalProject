package com.sylvie.boardgameguide.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.ItemPlayerFilterBinding

class NewPostPlayerFilterAdapter(var viewModel: NewPostViewModel):
    ListAdapter<User, NewPostPlayerFilterAdapter.PlayerViewHolder>(DiffCallback) {

//    class OnClickListener(val clickListener: (user: User) -> Unit) {
//        fun onClick(user: User) = clickListener(user)
//    }

    class PlayerViewHolder(private val binding: ItemPlayerFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, viewModel: NewPostViewModel) {
            binding.user = user
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            ItemPlayerFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}