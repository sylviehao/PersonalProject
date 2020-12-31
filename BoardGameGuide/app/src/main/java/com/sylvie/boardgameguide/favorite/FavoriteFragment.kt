package com.sylvie.boardgameguide.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.databinding.FragmentFavoriteBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.GameFragmentDirections

class FavoriteFragment : Fragment() {

    val viewModel by viewModels<FavoriteViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = FavoriteAdapter(FavoriteAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, viewModel)
        binding.recyclerFavorite.adapter = adapter

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(GameFragmentDirections.actionGlobalGameDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        viewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {user->
                adapter.submitList(user.favorite)
            }
        })

        binding.constraintAnimation.setOnClickListener {
            findNavController().navigate(FavoriteFragmentDirections.actionGlobalGameFragment())
        }

        return binding.root
    }
}