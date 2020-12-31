package com.sylvie.boardgameguide.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.databinding.FragmentProfileBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.GameFragmentDirections
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.profile.post.ProfilePostFragmentDirections

class ProfileFragment : Fragment() {

    val viewModel by viewModels<ProfileViewModel> {
        getVmFactory(
            ProfileFragmentArgs.fromBundle(
                requireArguments()
            ).userId
        )
    }
    lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val userId = ProfileFragmentArgs.fromBundle(requireArguments()).userId

        val adapter = ProfileBrowseAdapter(ProfileBrowseAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        setEditPermission(userId)

        binding.recyclerBrowse.adapter = adapter

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    GameFragmentDirections.actionGlobalGameDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })

        viewModel.allEventsData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEvent(it)
        })

        viewModel.myEventData.observe(viewLifecycleOwner, Observer {
            viewModel.filterMyEventStatus(it)
        })

        viewModel.userData.observe(viewLifecycleOwner, Observer {
            val gameIdList = mutableListOf<String>()
            it.browseRecently?.sortByDescending { browseList -> browseList.time }
            it.browseRecently?.forEach { browseRecently ->
                gameIdList.add(browseRecently.gameId)
            }
            viewModel.getBrowseRecently(gameIdList)
        })

        viewModel.browseRecentlyInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        binding.constraintPost.setOnClickListener {
            findNavController().navigate(
                ProfilePostFragmentDirections.actionGlobalProfilePostFragment(userId)
            )
        }

        binding.constraintEvent.setOnClickListener {
            findNavController().navigate(
                ProfilePostFragmentDirections.actionGlobalProfileEventFragment(userId)
            )
        }

        binding.buttonEditInfo.setOnClickListener {
            viewModel.userData.value?.let {
                findNavController().navigate(
                    ProfileFragmentDirections.actionGlobalProfileEditDialog(it)
                )
            }
        }

        binding.constraintAnimation.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionGlobalGameFragment())
        }
        
        return binding.root
    }

    private fun setEditPermission(userId: String) {
        if (userId != UserManager.userToken) {
            binding.buttonEditInfo.visibility = View.GONE
        } else {
            binding.buttonEditInfo.visibility = View.VISIBLE
        }
    }
}