package com.sylvie.boardgameguide.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentToolsBinding.inflate(inflater,container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.iconDice.setOnClickListener {
            findNavController().navigate(R.id.action_global_diceFragment)
        }


        return binding.root
    }
}