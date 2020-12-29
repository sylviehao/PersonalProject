package com.sylvie.boardgameguide.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robertlevonyan.views.chip.OnSelectClickListener
import com.sylvie.boardgameguide.databinding.ItemGameTypeBinding
import com.sylvie.boardgameguide.databinding.ItemHomePhotoBinding

class NewPostGameTypeAdapter(var viewModel: NewPostViewModel):
    ListAdapter<String, NewPostGameTypeAdapter.PhotoViewHolder>(DiffCallback) {

    class PhotoViewHolder(private val binding: ItemGameTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String, viewModel: NewPostViewModel) {
            binding.data = data

            binding.textGameType.onSelectClickListener = OnSelectClickListener { v, selected ->
                if(selected){
                    viewModel.addType(data, selected)
                }else{
                    viewModel.addType(data, selected)
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemGameTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}