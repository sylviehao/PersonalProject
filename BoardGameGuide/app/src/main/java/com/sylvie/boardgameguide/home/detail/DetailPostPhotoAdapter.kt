package com.sylvie.boardgameguide.home.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.data.PhotoItem
import com.sylvie.boardgameguide.databinding.ItemDetailPostPhotoBinding
import com.sylvie.boardgameguide.databinding.ItemEmptyPhotoBinding

class DetailPostPhotoAdapter(
    private val onClickListener: OnClickListener,
    var viewModel: DetailPostViewModel
) :
    ListAdapter<PhotoItem, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (event: String) -> Unit) {
        fun onClick(event: String) = clickListener(event)
    }

    class PhotoViewHolder(private val binding: ItemDetailPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    class EmptyPhotoViewHolder(private val binding: ItemEmptyPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: String,
            onClickListener: DetailPostPhotoAdapter.OnClickListener,
            viewModel: DetailPostViewModel
        ) {
            binding.data = data
            viewModel.photoPermission.let {
                if (it.value!!) {
                    binding.root.visibility = View.VISIBLE
                } else {
                    binding.root.visibility = View.GONE
                }
            }
            binding.imageAddPhoto.setOnClickListener { onClickListener.onClick(data) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DetailPostPhotoAdapter.ITEM_VIEW_TYPE_FULL -> PhotoViewHolder(
                ItemDetailPostPhotoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            DetailPostPhotoAdapter.ITEM_VIEW_TYPE_EMPTY -> EmptyPhotoViewHolder(
                ItemEmptyPhotoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyPhotoViewHolder -> {
                holder.bind(
                    (getItem(position) as PhotoItem.EmptyItem).event,
                    onClickListener,
                    viewModel
                )
            }
            is PhotoViewHolder -> {
                holder.bind((getItem(position) as PhotoItem.FullItem).event)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is PhotoItem.EmptyItem -> ITEM_VIEW_TYPE_EMPTY
            is PhotoItem.FullItem -> ITEM_VIEW_TYPE_FULL
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }

        private const val ITEM_VIEW_TYPE_EMPTY = 0x00
        private const val ITEM_VIEW_TYPE_FULL = 0x01
    }
}