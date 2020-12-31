package com.sylvie.boardgameguide.profile.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.databinding.FragmentProfileEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeFragmentDirections

class ProfileEventFragment : Fragment() {

    val viewModel by viewModels<ProfileEventViewModel> {
        getVmFactory(
            ProfileEventFragmentArgs.fromBundle(requireArguments()).userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ProfileEventAdapter(ProfileEventAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })
        binding.recyclerEvent.adapter = adapter

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalDetailEventFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })

        viewModel.allEventsData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEvent(it)
        })

        viewModel.myEventData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}