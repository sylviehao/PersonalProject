package com.sylvie.boardgameguide.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentDetailEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.detail.GameDetailFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*

class DetailEventFragment : Fragment() {

    val viewModel by viewModels<DetailEventViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailEventBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val bundle = DetailEventFragmentArgs.fromBundle(requireArguments()).event
        viewModel.getEventData.value = bundle
        viewModel.getGame(bundle.gameId)

        val adapter = DetailEventPlayerAdapter()
        val adapter2 = DetailEventPhotoAdapter()
        binding.recyclerPlayer.adapter = adapter
        binding.recyclerPhoto.adapter = adapter2
        adapter.submitList(bundle.playerList)
        adapter2.submitList(bundle.image)

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }

        binding.buttonJoin.setOnClickListener {
            //判斷是否加入過
            findNavController().navigate(R.id.action_global_joinDialog)
        }

        binding.textStatus.setOnClickListener {
            findNavController().navigate(DetailEventFragmentDirections.actionGlobalNewPostFragment(viewModel.getGameData.value, bundle))
        }



        return binding.root
    }

    override fun onStart() {
        super.onStart()
        let {
//            requireActivity().toolbar.visibility = View.GONE
            requireActivity().bottomNavView.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        let {
//            requireActivity().toolbar.visibility = View.VISIBLE
            requireActivity().bottomNavView.visibility = View.VISIBLE
        }
    }
}