package com.sylvie.boardgameguide.detailEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentDetailEventBinding
import com.sylvie.boardgameguide.detailPost.DetailPostFragmentArgs
import com.sylvie.boardgameguide.detailPost.DetailPostPlayerAdapter
import com.sylvie.boardgameguide.detailPost.DetailPostViewModel
import com.sylvie.boardgameguide.ext.getVmFactory
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
        binding.recyclerPlayer.adapter = adapter
        adapter.submitList(bundle.playerList)

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
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