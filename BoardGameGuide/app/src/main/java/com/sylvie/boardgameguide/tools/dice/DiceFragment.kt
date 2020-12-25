package com.sylvie.boardgameguide.tools.dice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sylvie.boardgameguide.databinding.ToolsDiceBinding

class DiceFragment : Fragment() {

    private lateinit var viewModel: DiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ToolsDiceBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(DiceViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = DiceAdapter(viewModel)
        binding.recyclerDice.adapter = adapter

        binding.buttonDice.setOnClickListener {
            viewModel.totalAmount.value = 0
            viewModel.rotateStatus.value = true
        }

        viewModel.rotateStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let{
                if(it){
                    adapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.amount.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                val amount = mutableListOf<Int>()
                for(i in 1 .. it){
                    amount.add(i)
                }
                adapter.submitList(amount)
            }

        })

        return binding.root
    }

}