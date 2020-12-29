package com.sylvie.boardgameguide.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.FragmentProfileBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.GameAdapter
import com.sylvie.boardgameguide.game.GameFragmentDirections
import com.sylvie.boardgameguide.game.GameViewModel
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.profile.post.ProfilePostFragmentDirections

class ProfileFragment : Fragment() {

    val viewModel by viewModels<ProfileViewModel> { getVmFactory(ProfileFragmentArgs.fromBundle(requireArguments()).userId) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val userId = ProfileFragmentArgs.fromBundle(requireArguments()).userId
        val adapter = ProfileBrowseAdapter(ProfileBrowseAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        if (userId != UserManager.userToken) {
            binding.buttonEditInfo.visibility = View.GONE
        } else {
            binding.buttonEditInfo.visibility = View.VISIBLE
        }

        binding.recyclerBrowse.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(GameFragmentDirections.actionGlobalGameDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        viewModel.getEventData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEvent(it)
        })

        viewModel.myEventData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEventStatus(it)
        })

        viewModel.getUserData.observe(viewLifecycleOwner, Observer {
            val gameIdList = mutableListOf<String>()
            it.browseRecently?.sortByDescending { browseList-> browseList.time }
            it.browseRecently?.forEach { browseRecently->
                gameIdList.add(browseRecently.gameId)
            }
             viewModel.getBrowseRecently(gameIdList)
        })

        viewModel.getBrowseRecentlyInfo.observe(viewLifecycleOwner, Observer {
            if (viewModel.getBrowseRecentlyInfo.value.isNullOrEmpty()) {
                binding.constraintAnimation.visibility = View.VISIBLE
                binding.recyclerBrowse.visibility = View.GONE
                binding.textBrowse.visibility = View.GONE
            } else {
                binding.constraintAnimation.visibility = View.INVISIBLE
                binding.recyclerBrowse.visibility = View.VISIBLE
                binding.textBrowse.visibility = View.VISIBLE
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        binding.constraintPost.setOnClickListener {
            findNavController().navigate(ProfilePostFragmentDirections.actionGlobalProfilePostFragment(userId))
        }

        binding.constraintEvent.setOnClickListener {
            findNavController().navigate(ProfilePostFragmentDirections.actionGlobalProfileEventFragment(userId))
        }

        binding.buttonEditInfo.setOnClickListener {
            viewModel.getUserData.value?.let {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalProfileEditDialog(it))
            }
        }

        binding.constraintAnimation.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionGlobalGameFragment())
        }

        return binding.root
    }
}