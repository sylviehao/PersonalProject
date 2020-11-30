package com.sylvie.boardgameguide.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.ItemHomePostBinding

class HomeAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Event, HomeAdapter.HomeViewHolder>(HomeAdapter.DiffCallback) {

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

    class HomeViewHolder(private val binding: ItemHomePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, onClickListener: OnClickListener) {
            binding.event = event
            binding.textHostName.text = event.hostId
            binding.textGameTopic.text = event.topic
            binding.textTotalPlayer.text = event.playerLimit.toString()
            binding.textTotalLike.text = event.like?.size.toString()
            binding.textGameName.text = event.gameId
            binding.root.setOnClickListener { onClickListener.onClick(event) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(ItemHomePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}