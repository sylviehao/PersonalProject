package com.sylvie.boardgameguide.home


import android.annotation.SuppressLint
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
import com.sylvie.boardgameguide.util.Util.getTimeDate
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(
    private val onClickListener: OnClickListener,
    private val viewModel: HomeViewModel
) :
    ListAdapter<HomeItem, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

    class PostViewHolder(private val binding: ItemHomePostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, onClickListener: OnClickListener, viewModel: HomeViewModel) {
            binding.event = event
            binding.textCreatedTime.text = getTimeDate(event.createdTime.toDate())
            binding.root.setOnClickListener { onClickListener.onClick(event) }
            binding.executePendingBindings()
        }
    }

    class EventViewHolder(private val binding: ItemHomeEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, onClickListener: OnClickListener, viewModel: HomeViewModel) {
            binding.event = event
            binding.textCreatedTime.text = getTimeDate(event.createdTime.toDate())
            binding.root.setOnClickListener { onClickListener.onClick(event) }
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_POST -> PostViewHolder(
                ItemHomePostBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            ITEM_VIEW_TYPE_EVENT -> EventViewHolder(
                ItemHomeEventBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> {
                holder.bind(
                    (getItem(position) as HomeItem.PostItem).event,
                    onClickListener,
                    viewModel
                )
            }
            is EventViewHolder -> {
                holder.bind(
                    (getItem(position) as HomeItem.EventItem).event,
                    onClickListener,
                    viewModel
                )
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

        private const val ITEM_VIEW_TYPE_POST = 0x00
        private const val ITEM_VIEW_TYPE_EVENT = 0x01
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.PostItem -> ITEM_VIEW_TYPE_POST
            is HomeItem.EventItem -> ITEM_VIEW_TYPE_EVENT
        }
    }
}

