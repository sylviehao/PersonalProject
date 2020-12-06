package com.sylvie.boardgameguide.game.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Game
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

        val db = FirebaseFirestore.getInstance()


        binding.buttonCreateEvent.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.actionGlobalNewEventFragment(bundle))
        }

        binding.buttonCreatePost.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.actionGlobalNewPostFragment(bundle))
        }

        viewModel.getUserData.observe(viewLifecycleOwner, Observer {
            if(it?.favorite!!.any { favorite -> favorite.id == bundle.id }){
                binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                binding.iconPin.tag = "select"
            }else{
                binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin)
                binding.iconPin.tag = "empty"
            }
        })


        binding.iconPin.setOnClickListener {
            if(it.tag == "empty"){
                it.tag = "select"
                it.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                viewModel.add2Favorite(bundle)
                viewModel.boomImage(binding.imageGame)
            }else{
                it.tag = "empty"
                it.setBackgroundResource(R.drawable.ic_nav_pin)
            }
        }


        return binding.root
    }
}