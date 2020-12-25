package com.sylvie.boardgameguide.game.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentDetailGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import me.samlss.bloom.Bloom
import me.samlss.bloom.effector.BloomEffector
import me.samlss.bloom.shape.distributor.CircleShapeDistributor
import me.samlss.bloom.shape.distributor.ParticleShapeDistributor
import me.samlss.bloom.shape.distributor.RectShapeDistributor
import me.samlss.bloom.shape.distributor.StarShapeDistributor

class GameDetailFragment : Fragment() {
    val viewModel by viewModels<GameDetailViewModel> { getVmFactory(GameDetailFragmentArgs.fromBundle(requireArguments()).game.id) }

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

        val adapter = GameDetailToolAdapter(viewModel)
        binding.recyclerTools.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        binding.buttonCreateEvent.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.actionGlobalNewEventFragment(bundle))
        }

        binding.buttonCreatePost.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.actionGlobalNewPostFragment(bundle, null))
        }

        viewModel.boomStatus.observe(viewLifecycleOwner, Observer {
            boom(it)
        })

        viewModel.getUserData.observe(viewLifecycleOwner, Observer {
            if(it?.favorite!!.any { favorite -> favorite.id == bundle.id }){
                binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin_selected)
                binding.iconPin.tag = "select"
            }else{
                binding.iconPin.setBackgroundResource(R.drawable.ic_nav_pin)
                binding.iconPin.tag = "empty"
            }
            if(it.browseRecently.isNullOrEmpty() || !it.browseRecently!!.any { browse -> browse.gameId == bundle.id } ){
                viewModel.setBrowseRecently()
            }
        })

        viewModel.getGameData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.tools)
        })


        viewModel.navigateToTool.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    "Dice" -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalDiceFragment())
                    "Timer" -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalTimerFragment())
                    else -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalBottleFragment())
                }
                viewModel.navigated()
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
                viewModel.removeFavorite(bundle)
                it.setBackgroundResource(R.drawable.ic_nav_pin)
            }
        }


        return binding.root
    }

    private var mBloom: Bloom? = null

    private fun boom(view: View) {
        val shapeDistributor: ParticleShapeDistributor = RectShapeDistributor()

        mBloom?.cancel()
        mBloom = Bloom.with(requireActivity())
            .setParticleRadius(15F)
            .setShapeDistributor(shapeDistributor)
            .setEffector(
                BloomEffector.Builder()
                    .setDuration(2000)
                    .setScaleRange(0.5f, 1.5f)
                    .setRotationSpeedRange(0.01f, 0.05f)
                    .setSpeedRange(0.1f, 0.5f)
                    .setAcceleration(0.00025f, 90)
                    .setAnchor((view.getWidth() / 2).toFloat(), view.getHeight().toFloat())
                    .setFadeOut(500, AccelerateInterpolator())
                    .build())
        mBloom?.boom(view)
    }
}