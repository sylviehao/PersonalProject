package com.sylvie.boardgameguide.home

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.madapps.liquid.LiquidRefreshLayout
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.FragmentHomeBinding
import com.sylvie.boardgameguide.ext.getVmFactory

class HomeFragment : Fragment() {

    val viewModel by viewModels<HomeViewModel> { getVmFactory() }
    private var flag = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, viewModel)
        binding.recyclerHome.adapter = adapter

        binding.refreshLayout.setOnRefreshListener(object : LiquidRefreshLayout.OnRefreshListener {
            override fun completeRefresh() {
            }
            override fun refreshing() {
                object : CountDownTimer(1800, 1000) {

                    override fun onFinish() {
                        binding.refreshLayout.finishRefreshing()
                        adapter.notifyDataSetChanged()
                    }
                    override fun onTick(millisUntilFinished: Long) {
                    }
                }.start()
            }
        })

        binding.fabPost.hide()
        binding.fabEvent.hide()

        binding.fab.setOnClickListener {
            flag = if (flag == 0) {
                binding.fabPost.show()
                binding.fabEvent.show()
                1
            } else {
                binding.fabPost.hide()
                binding.fabEvent.hide()
                0
            }
        }

        binding.fabPost.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalNewPostFragment(null, null))
        }

        binding.fabEvent.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalNewEventFragment(null))
        }

        binding.searchView.addTextChangedListener {
            viewModel.getEventData.value?.let {eventList->
                val filterList = viewModel.filter(eventList, binding.searchView.text.toString())
                adapter.submitList(filterList)
            }
        }

        viewModel.getHome.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getEvents()
                if(it.status == "CLOSE"){
                    findNavController().navigate(HomeFragmentDirections.actionGlobalDetailPostFragment(it))
                } else {
                    findNavController().navigate(HomeFragmentDirections.actionGlobalDetailEventFragment(it))
                }
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }
}