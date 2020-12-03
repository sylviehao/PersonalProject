package com.sylvie.boardgameguide.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.HomeItem
import com.sylvie.boardgameguide.databinding.ItemHomeEventBinding
import com.sylvie.boardgameguide.databinding.ItemHomePostBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<HomeItem, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

    class PostViewHolder(private val binding: ItemHomePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, onClickListener: OnClickListener) {
            binding.event = event
            binding.textHostName.text = event.hostId
            binding.textGameTopic.text = event.topic
            binding.textTotalPlayer.text = event.playerLimit.toString()
            binding.textTotalLike.text = event.like?.size.toString()
            binding.textGameName.text = event.gameId
            binding.imageGamePicture.setBackgroundResource(R.drawable.pic_green_leaf)
            binding.textCreatedTime.text = getTimeDate(event.createdTime.toDate())
            binding.root.setOnClickListener { onClickListener.onClick(event) }
            binding.executePendingBindings()
        }
    }

    class EventViewHolder(private val binding: ItemHomeEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, onClickListener: OnClickListener) {
            binding.event = event
            binding.textHostName.text = event.hostId
            binding.textGameTopic.text = event.topic
            binding.textGameName.text = event.gameId
            binding.imageGamePicture.setBackgroundResource(R.drawable.pic_green_leaf)
            binding.textCreatedTime.text = getTimeDate(event.createdTime.toDate())
            binding.root.setOnClickListener { onClickListener.onClick(event) }
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_POST -> PostViewHolder(ItemHomePostBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            ))
            ITEM_VIEW_TYPE_EVENT -> EventViewHolder(ItemHomeEventBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            ))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> {
                holder.bind((getItem(position) as HomeItem.PostItem).event, onClickListener)
            }
            is EventViewHolder -> {
                holder.bind((getItem(position) as HomeItem.EventItem).event, onClickListener)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HomeItem>() {
        override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return oldItem == newItem
        }

        private const val ITEM_VIEW_TYPE_POST   = 0x00
        private const val ITEM_VIEW_TYPE_EVENT  = 0x01
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.PostItem -> ITEM_VIEW_TYPE_POST
            is HomeItem.EventItem -> ITEM_VIEW_TYPE_EVENT
        }
    }
}

fun getTimeDate(timestamp: Date): String {
    try {
        val netDate = (timestamp)
        val sfd = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.TAIWAN)
        return sfd.format(netDate)
    } catch (e: Exception) {
        return "date"
    }
}