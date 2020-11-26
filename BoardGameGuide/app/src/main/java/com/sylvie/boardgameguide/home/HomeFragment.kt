package com.sylvie.boardgameguide.home

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.madapps.liquid.LiquidRefreshLayout
import com.sylvie.boardgameguide.databinding.HomeFragmentBinding
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = HomeFragmentBinding.inflate(inflater, container,false)
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



        return binding.root
    }
}