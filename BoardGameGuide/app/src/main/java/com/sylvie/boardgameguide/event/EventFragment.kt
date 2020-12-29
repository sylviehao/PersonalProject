package com.sylvie.boardgameguide.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.FragmentEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.detail.GameDetailFragmentDirections
import com.sylvie.boardgameguide.home.HomeFragmentDirections
import com.sylvie.boardgameguide.login.UserManager

class EventFragment : Fragment() {

    val viewModel by viewModels<EventViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = EventAdapter(EventAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, viewModel)

        binding.recyclerEvent.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalDetailEventFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        viewModel.getEventData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEvent(it)
        })

        viewModel.myEventData.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (viewModel.myEventData.value.isNullOrEmpty()) {
                    binding.constraintAnimation.visibility = View.VISIBLE
                    binding.recyclerEvent.visibility = View.INVISIBLE
                } else {
                    binding.constraintAnimation.visibility = View.INVISIBLE
                    binding.recyclerEvent.visibility = View.VISIBLE
                    adapter.submitList(it)
                }
            }
        })

        return binding.root
    }
}