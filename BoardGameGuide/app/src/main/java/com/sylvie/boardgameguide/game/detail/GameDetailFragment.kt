package com.sylvie.boardgameguide.game.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.FragmentDetailGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.util.Util.boom

class GameDetailFragment : Fragment() {
    val viewModel by viewModels<GameDetailViewModel> {
        getVmFactory(
            GameDetailFragmentArgs.fromBundle(
                requireArguments()
            ).game.id
        )
    }
    lateinit var binding : FragmentDetailGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailGameBinding.inflate(inflater, container, false)
        val bundle = GameDetailFragmentArgs.fromBundle(requireArguments()).game
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.gameData.value = bundle

        val adapter = GameDetailToolAdapter(viewModel)
        binding.recyclerTools.adapter = adapter

        binding.buttonCreateEvent.setOnClickListener {
            findNavController().navigate(
                GameDetailFragmentDirections.actionGlobalNewEventFragment(bundle)
            )
        }

        binding.buttonCreatePost.setOnClickListener {
            findNavController().navigate(
                GameDetailFragmentDirections.actionGlobalNewPostFragment(bundle, null)
            )
        }

        binding.iconPin.setOnClickListener {
            changeFavoriteStatus(it, bundle)
        }

        viewModel.boomStatus.observe(viewLifecycleOwner, Observer {
            boom(it, requireActivity())
        })

        viewModel.userData.observe(viewLifecycleOwner, Observer {
            reloadFavorite(it, bundle)
            checkBrowseRecently(it, bundle)
        })

        viewModel.gameData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.tools)
        })

        viewModel.toolsNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    "Dice" -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalDiceFragment())
                    "Timer" -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalTimerFragment())
                    else -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalBottleFragment())
                }
                viewModel.navigated()
            }
        })

        return binding.root
    }

    private fun checkBrowseRecently(
        user: User,
        bundle: Game
    ) {
        if (user.browseRecently.isNullOrEmpty() || !user.browseRecently!!
                .any { browse -> browse.gameId == bundle.id }
        ) {
            viewModel.setBrowseRecently()
        }
    }

    private fun reloadFavorite(
        user: User?,
        bundle: Game
    ) {
        if (user?.favorite!!.any { favorite -> favorite.id == bundle.id }) {
            binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin_selected)
            binding.iconPin.tag = "select"
        } else {
            binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin)
            binding.iconPin.tag = "empty"
        }
    }

    private fun changeFavoriteStatus(
        it: View,
        bundle: Game
    ) {
        if (it.tag == "empty") {
            it.tag = "select"
            it.setBackgroundResource(R.drawable.ic_nav_pin_selected)
            viewModel.add2Favorite(bundle)
            viewModel.boomImage(binding.imageGame)
        } else {
            it.tag = "empty"
            viewModel.removeFavorite(bundle)
            it.setBackgroundResource(R.drawable.ic_nav_pin)
        }
    }
}