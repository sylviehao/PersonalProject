package com.sylvie.boardgameguide.game.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.databinding.FragmentDetailGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory

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
        var bundle = GameDetailFragmentArgs.fromBundle(requireArguments()).game
        viewModel.getGameData.value = bundle


        binding.buttonCreateEvent.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.actionGlobalNewEventFragment(bundle))
        }

        binding.buttonCreatePost.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.actionGlobalNewPostFragment(bundle))
        }


        return binding.root
    }
}