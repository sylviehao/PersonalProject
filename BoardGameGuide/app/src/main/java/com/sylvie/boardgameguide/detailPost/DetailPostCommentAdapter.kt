package com.sylvie.boardgameguide.detailPost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.ItemDetailPostCommentBinding
import com.sylvie.boardgameguide.databinding.ItemGameBinding

class DetailPostCommentAdapter:
    ListAdapter<Message, DetailPostCommentAdapter.CommentViewHolder>(DetailPostCommentAdapter.DiffCallback) {

    class CommentViewHolder(private val binding: ItemDetailPostCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.message = message
            binding.textUser.text = message.userId
            binding.textMessage.text = message.message
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemDetailPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}