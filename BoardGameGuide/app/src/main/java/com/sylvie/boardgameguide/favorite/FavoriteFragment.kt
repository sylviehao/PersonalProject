package com.sylvie.boardgameguide.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}