package com.sylvie.boardgameguide.tools.bottle

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.MainActivity
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.databinding.ToolsBottleBinding
import kotlinx.android.synthetic.main.fragment_profile.*

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
            onClick(binding.imageBottle)
        }


        return binding.root
    }

    val animListener = object: Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            Log.d("bottle", "onAnimationStart")
        }

        override fun onAnimationEnd(animation: Animation?) {
            Log.d("bottle", "onAnimationEnd")
            var value = ""
            if (stop < 5)
            {
                value = "A"
            }
            else if (5 <= stop && stop < 15)
            {
                value = "B"
            }
            else if (15 <= stop && stop < 30)
            {
                value = "C"
            }
            else if (30 <= stop && stop < 45)
            {
                value = "D"
            }
            else if (45 <= stop && stop < 65)
            {
                value = "E"
            }
            else if (65 <= stop && stop < 100)
            {
                value = "F"
            }
            else
            {
                Log.d("bottle", "Should be not here")
            }
            Toast.makeText(context, "Result = " + value, Toast.LENGTH_SHORT).show()
        }

        override fun onAnimationRepeat(animation: Animation?) {
            Log.d("bottle", "onAnimationRepeat")
        }
    }

    fun onClick(view: View) {
        stop = (Math.random() * 100).toInt()
        Log.d("bottle", "Stop = " + stop)
        result = prosibility(stop)
        Log.d("bottle", "After prosibility: temp = " + result)
        val am = RotateAnimation(
            0f, (3600 + result).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        am.setAnimationListener(animListener)
        am.setDuration(3000)
        am.setFillAfter(true)
        binding.imageBottle.startAnimation(am)
    }

    private fun prosibility(stop: Int): Int {
        if (stop < 5) {
            return 360
        } else if (5 <= stop && stop < 15) {
            return 300
        } else if (15 <= stop && stop < 30) {
            return 240
        }
        else if (30 <= stop && stop < 45)
        {
            return 180
        }
        else if (45 <= stop && stop < 65)
        {
            return 120
        }
        else if (65 <= stop && stop < 100)
        {
            return 60
        }
        else
        {
            Log.d("bottle", "Should be not here")
            return 0
        }
    }
}