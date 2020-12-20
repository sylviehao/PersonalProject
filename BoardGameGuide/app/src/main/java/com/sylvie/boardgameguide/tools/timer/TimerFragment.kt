package com.sylvie.boardgameguide.tools.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.ToolsTimerBinding
import java.sql.Time
import java.util.*

class TimerFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    var timerStatus : Boolean = false
    lateinit var binding: ToolsTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ToolsTimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.constraintTimer.setOnClickListener {
            timerStatus = if(timerStatus){
                timer.cancel()
                false
            }else{
                createTimer(binding.textTime.text.toString().toFloat())
                timer.start()
                true
            }
        }

        binding.text1.setOnClickListener {
            resetCount(binding.text1.text.toString())
        }

        binding.text2.setOnClickListener {
            resetCount(binding.text2.text.toString())
        }

        binding.text3.setOnClickListener {
            resetCount(binding.text3.text.toString())
        }

        binding.textCustom.setOnClickListener {

        }

        return binding.root
    }


    private fun createTimer(time: Float){
        val second = 1000L
        binding.circularProgressBar.progressMax = time

        timer = object : CountDownTimer(time.toLong() * second, 10) {

            override fun onFinish() {
                binding.textTime.text = "0"
                timerStatus = false
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.textTime.text = (millisUntilFinished/1000).toString()
                binding.circularProgressBar.progress = (time * 1000 - millisUntilFinished)/1000
            }
        }
    }
    private fun resetCount(time: String){
        timer.cancel()
        timerStatus = false
        binding.circularProgressBar.progress = 0F
        binding.textTime.text = time
    }
}