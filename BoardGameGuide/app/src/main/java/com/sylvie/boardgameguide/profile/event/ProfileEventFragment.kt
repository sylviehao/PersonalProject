package com.sylvie.boardgameguide.profile.event

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
import com.sylvie.boardgameguide.databinding.FragmentProfileEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeFragmentDirections
import com.sylvie.boardgameguide.profile.post.ProfilePostAdapter

class ProfileEventFragment : Fragment() {

    val viewModel by viewModels<ProfileEventViewModel> { getVmFactory() }
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

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalDetailEventFragment(it))
                viewModel.onDetailNavigated()
            }
        })

//        viewModel.getEventData.observe(viewLifecycleOwner, Observer {
//            adapter.submitList(it)
//        })

        val db = FirebaseFirestore.getInstance()

        //即時監聽資料庫是否變動
        db.collection("Event").whereEqualTo("status","OPEN")
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()
                    val listResultOpen = mutableListOf<Event>()
                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)
                    }
                    listResult.sortByDescending { it.createdTime }

                    listResultOpen.addAll( listResult.filter {list ->
                        list.playerList!!.any { name -> name == "sylviehao" }
                    })
                    adapter.submitList(listResult)
                }
            }


        return binding.root
    }
}