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
import com.sylvie.boardgameguide.detailPost.DetailPostViewModel
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeAdapter
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
        })

        binding.recyclerEvent.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                    findNavController().navigate(HomeFragmentDirections.actionGlobalDetailEventFragment(it))
                viewModel.onDetailNavigated()
            }
        })

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

                    adapter.submitList(listResultOpen)
                }
            }

        return binding.root
    }
}