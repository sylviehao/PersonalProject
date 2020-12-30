package com.sylvie.boardgameguide.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentToolsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.iconDice.setOnClickListener {
            findNavController().navigate(R.id.action_global_diceFragment)
        }

        binding.iconTimer.setOnClickListener {
            findNavController().navigate(R.id.action_global_timerFragment)
        }

        binding.iconPick.setOnClickListener {
            findNavController().navigate(R.id.action_global_bottleFragment)
        }

        binding.animationView.startAnimation(
            AnimationUtils.loadAnimation(
                binding.root.context,
                R.anim.anim_slide_up
            )
        )
        binding.animationAstronaut.visibility = View.VISIBLE
        binding.animationAstronaut.startAnimation(
            AnimationUtils.loadAnimation(
                binding.root.context,
                R.anim.anim_slide_right
            )
        )

        return binding.root
    }
}