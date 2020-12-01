package com.sylvie.boardgameguide.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.FragmentEventBinding
import com.sylvie.boardgameguide.detailPost.DetailPostViewModel
import com.sylvie.boardgameguide.ext.getVmFactory

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
        val adapter = EventAdapter()
        binding.recyclerEvent.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        //即時監聽資料庫是否變動
        db.collection("Event").addSnapshotListener { value, error ->
            value?.let {
                val listResult = mutableListOf<Event>()
                it.forEach { data ->
                    val d = data.toObject(Event::class.java)
                    listResult.add(d)
                }
                listResult.sortByDescending { it.createdTime }
                adapter.submitList(listResult)
            }
        }

        return binding.root
    }
}