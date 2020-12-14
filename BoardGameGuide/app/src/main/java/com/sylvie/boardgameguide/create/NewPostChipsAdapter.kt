package com.sylvie.boardgameguide.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sylvie.boardgameguide.R

//class ChipsAdapter : RecyclerView.Adapter<ChipsAdapter.ChipViewHolder>(){
//o;
//    val chipsContents: MutableList<String> = mutableListOf()
//
//    inner class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(chipContent: String) {
//            with(itemView) {
//                chipTextView.text = chipContent
//                removeChipButton.setOnClickListener {
//                    val index = chipsContents.indexOf(chipContent)
//                    chipsContents.remove(chipContent)
//                    notifyItemRemoved(index)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.chip_item, parent, false)
//        return ChipViewHolder(v)
//    }
//
//    override fun getItemCount() = chipsContents.size
//
//    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) = holder.bind(chipsContents[position])
//}