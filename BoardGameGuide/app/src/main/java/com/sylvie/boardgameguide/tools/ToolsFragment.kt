package com.sylvie.boardgameguide.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentToolsBinding.inflate(inflater,container, false)
        binding.lifecycleOwner = viewLifecycleOwner



        return binding.root
    }
}