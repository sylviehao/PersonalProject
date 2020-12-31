package com.sylvie.boardgameguide.profile.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.databinding.FragmentProfilePostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeFragmentDirections
import com.sylvie.boardgameguide.profile.event.ProfileEventFragmentArgs

class ProfilePostFragment : Fragment() {

    val viewModel by viewModels<ProfilePostViewModel> {
        getVmFactory(
            ProfilePostFragmentArgs.fromBundle(
                requireArguments()
            ).userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfilePostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ProfilePostAdapter(ProfilePostAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        binding.recyclerPost.adapter = adapter

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalDetailEventFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })

        viewModel.allEventsData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyPost(it)
        })

        viewModel.myPostData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}