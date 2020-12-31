package com.sylvie.boardgameguide.tools.bottle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.databinding.ToolsBottleBinding

class BottleFragment : Fragment() {

    private var stop = 0
    private var result = 0
    lateinit var binding: ToolsBottleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ToolsBottleBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonBottle.setOnClickListener {
            turn(binding.imageBottle)
        }

        return binding.root
    }

    private val animListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            Log.d("bottle", "onAnimationStart")
        }

        override fun onAnimationEnd(animation: Animation?) {
            Log.d("bottle", "onAnimationEnd")
            var value = ""
            if (stop < 3) {
                value = "A"
            } else if (stop in 4..7) {
                value = "B"
            } else if (stop in 8..14) {
                value = "C"
            } else if (stop in 15..21) {
                value = "D"
            } else if (stop in 22..32) {
                value = "E"
            } else if (stop in 33..45) {
                value = "F"
            } else if (stop in 46..56) {
                value = "G"
            } else if (stop in 57..68) {
                value = "H"
            } else if (stop in 69..70) {
                value = "I"
            } else if (stop in 71..82) {
                value = "J"
            } else if (stop in 83..90) {
                value = "K"
            } else if (stop in 91..99) {
                value = "L"
            } else {
                Log.d("bottle", "Should be not here")
            }
        }

        override fun onAnimationRepeat(animation: Animation?) {
            Log.d("bottle", "onAnimationRepeat")
        }
    }

    private fun turn(view: View) {
        stop = (Math.random() * 100).toInt()
        Log.d("bottle", "Stop = $stop")
        result = possibility(stop)
        Log.d("bottle", "After possibility: temp = $result")
        val am = RotateAnimation(
            0f, (3600 + result).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        am.setAnimationListener(animListener)
        am.duration = 3000
        am.fillAfter = true
        binding.imageBottle.startAnimation(am)
    }

    private fun possibility(stop: Int): Int {
        when {
            stop < 3 -> {
                return 360
            }
            stop in 4..7 -> {
                return 330
            }
            stop in 8..14 -> {
                return 300
            }
            stop in 15..21 -> {
                return 270
            }
            stop in 22..32 -> {
                return 240
            }
            stop in 33..45 -> {
                return 210
            }
            stop in 46..56 -> {
                return 180
            }
            stop in 57..68 -> {
                return 150
            }
            stop in 69..70 -> {
                return 120
            }
            stop in 71..82 -> {
                return 90
            }
            stop in 83..90 -> {
                return 60
            }
            stop in 91..99 -> {
                return 30
            }
            else -> {
                Log.d("bottle", "Should be not here")
                return 0
            }
        }
    }
}