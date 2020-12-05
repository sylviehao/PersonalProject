package com.sylvie.boardgameguide.profile.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.ItemHomeEventBinding
import com.sylvie.boardgameguide.home.getTimeDate

class ProfileEventAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Event, ProfileEventAdapter.ProfileEventViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

    class ProfileEventViewHolder(private val binding: ItemHomeEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, onClickListener: OnClickListener) {
            binding.event = event
            binding.textHostName.text = event.hostId
            binding.textGameName.text = event.gameId
            binding.textGameTopic.text = event.topic
            binding.textGameTime.text = event.time.toString()
            binding.textGameLocation.text = event.location
            binding.imageGamePicture.setBackgroundResource(R.drawable.pic_green_leaf)
            binding.textCreatedTime.text = getTimeDate(event.createdTime.toDate())
            binding.root.setOnClickListener { onClickListener.onClick(event) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileEventViewHolder {
        return ProfileEventViewHolder(
            ItemHomeEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProfileEventViewHolder, position: Int) {
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