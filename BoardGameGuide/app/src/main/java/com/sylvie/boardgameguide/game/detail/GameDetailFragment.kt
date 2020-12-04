package com.sylvie.boardgameguide.game.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sylvie.boardgameguide.databinding.FragmentDetailGameBinding
import com.sylvie.boardgameguide.detail.DetailPostFragmentArgs
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.GameViewModel

class GameDetailFragment : Fragment() {
    val viewModel by viewModels<GameDetailViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  FragmentDetailGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val bundle = GameDetailFragmentArgs.fromBundle(requireArguments()).game
        viewModel.getGameData.value = bundle


        return binding.root
    }
}