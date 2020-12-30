package com.sylvie.boardgameguide.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.databinding.FragmentEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeFragmentDirections

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

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalDetailEventFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        viewModel.allEventsData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEvent(it)
        })

        viewModel.myEventsData.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (viewModel.myEventsData.value.isNullOrEmpty()) {
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