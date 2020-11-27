package com.sylvie.boardgameguide.home

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.madapps.liquid.LiquidRefreshLayout
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var isMenuOpen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = HomeAdapter()
        binding.recyclerHome.adapter = adapter

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
            findNavController().navigate(R.id.action_global_newPostFragment)
        }

        binding.fabEvent.setOnClickListener {
            findNavController().navigate(R.id.action_global_newEventFragment)
        }

        val db = FirebaseFirestore.getInstance()

        //即時監聽資料庫是否變動
        db.collection("Event").addSnapshotListener { value, error ->
            value?.let {
                val listResult = mutableListOf<Event>()
                it.forEach { data ->
                    val d = data.toObject(Event::class.java)
                    listResult.add(d)

                    Log.i("REALTIMETAG", "${data.data}")

                }
//                listResult.sortByDescending { it.createdTime }
                adapter.submitList(listResult)
            }
        }




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