package com.sylvie.boardgameguide.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.FragmentGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeAdapter
import com.sylvie.boardgameguide.home.HomeFragmentDirections
import me.samlss.bloom.Bloom
import me.samlss.bloom.effector.BloomEffector
import me.samlss.bloom.particle.BloomParticle
import me.samlss.bloom.shape.ParticleShape
import me.samlss.bloom.shape.ParticleStarShape
import me.samlss.bloom.shape.distributor.CircleShapeDistributor
import me.samlss.bloom.shape.distributor.ParticleShapeDistributor
import me.samlss.bloom.shape.distributor.RectShapeDistributor
import me.samlss.bloom.shape.distributor.StarShapeDistributor
import java.lang.reflect.Array.set

class GameFragment : Fragment() {

    val viewModel by viewModels<GameViewModel> { getVmFactory() }
    lateinit var binding: FragmentGameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val db = FirebaseFirestore.getInstance()

        val adapter = GameAdapter(GameAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, viewModel)

        binding.recyclerGame.adapter = adapter


        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                    findNavController().navigate(GameFragmentDirections.actionGlobalGameDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        viewModel.boomStatus.observe(viewLifecycleOwner, Observer {
            boom(it)
        })


        viewModel.getUserData.observe(viewLifecycleOwner, Observer {
            db.collection("Game").addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Game>()
                    it.forEach { data ->
                        val d = data.toObject(Game::class.java)
                        listResult.add(d)
                    }
                    adapter.submitList(listResult)
                }
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_global_newGameFragment)
        }


        binding.searchView.addTextChangedListener {
            viewModel.getGameData.value?.let {gameList->
                val filterList = viewModel.filter(gameList, binding.searchView.text.toString())
                adapter.submitList(filterList)
            }
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }



    private var mBloom: Bloom? = null

    private fun boom(view: View) {
        val shapeDistributor: ParticleShapeDistributor = CircleShapeDistributor()

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