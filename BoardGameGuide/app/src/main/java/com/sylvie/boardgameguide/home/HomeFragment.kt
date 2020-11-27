package com.sylvie.boardgameguide.home

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.madapps.liquid.LiquidRefreshLayout
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner

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

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_global_newPostFragment)
        }





        return binding.root
    }
}