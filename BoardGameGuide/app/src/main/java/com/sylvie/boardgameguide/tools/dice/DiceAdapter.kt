package com.sylvie.boardgameguide.tools.dice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.databinding.ItemDiceBinding

class DiceAdapter(var viewModel: DiceViewModel) : ListAdapter<Int, DiceAdapter.GameViewHolder>(DiffCallback) {

    class GameViewHolder(private val binding: ItemDiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int, viewModel: DiceViewModel) {
//            binding.item = item
            if(viewModel.text.value == true){
                binding.imageDice.setImageResource(viewModel.rollDice())
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemDiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}