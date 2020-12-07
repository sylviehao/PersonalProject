package com.sylvie.boardgameguide.home

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var isMenuOpen = false
    val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })
        binding.recyclerHome.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.status == "CLOSE"){
                    findNavController().navigate(HomeFragmentDirections.actionGlobalDetailPostFragment(it))
                } else {
                    findNavController().navigate(HomeFragmentDirections.actionGlobalDetailEventFragment(it))
                }
                viewModel.onDetailNavigated()
            }
        })

        binding.refreshLayout.setOnRefreshListener(object : LiquidRefreshLayout.OnRefreshListener {
            override fun completeRefresh() {
            }
            override fun refreshing() {
//                adapter.notifyDataSetChanged()
                object : CountDownTimer(2000, 1000) {

                    override fun onFinish() {
                        binding.refreshLayout.finishRefreshing()
                    }
                    override fun onTick(millisUntilFinished: Long) {
                    }
                }.start()
            }
        })

        binding.fabPost.hide()
        binding.fabEvent.hide()

        binding.fab.setOnClickListener {
            binding.fabPost.show()
            binding.fabEvent.show()
        }

        binding.fabPost.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalNewPostFragment(null))
        }

        binding.fabEvent.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalNewEventFragment(null))
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable?) {
                Log.d("textChange", "$s")
                viewModel.getEventData.value?.let {eventList->
                    val filterList = viewModel.filter(eventList, binding.searchView.text.toString())
                    adapter.submitList(filterList)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })



//        val db = FirebaseFirestore.getInstance()
//
//        //即時監聽資料庫是否變動
//        db.collection("Event").addSnapshotListener { value, error ->
//            value?.let {
//                val listResult = mutableListOf<Event>()
//                it.forEach { data ->
//                    val d = data.toObject(Event::class.java)
//                    listResult.add(d)
//                }
//                listResult.sortByDescending { it.createdTime }
//                adapter.submitList(listResult)
//            }
//        }

//        viewModel.getEventData.observe(viewLifecycleOwner, Observer {
//            adapter.submitList(it)
//        })

        viewModel.getHome.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })



//        fun openFabMenu() {
//            isMenuOpen = true
//            binding.fabPost.show()
//            binding.fabEvent.show()
//        }
//
//        fun closeFabMenu() {
//            isMenuOpen = false
//            binding.fabPost.hide()
//            binding.fabEvent.hide()
//        }


        return binding.root
    }


}