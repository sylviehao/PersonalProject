package com.sylvie.boardgameguide.tools.dice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.ToolsDiceBinding
import kotlinx.android.synthetic.main.tools_dice.*
import java.util.*

class DiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ToolsDiceBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonDice.setOnClickListener {
            rollDice()
        }

        return binding.root
    }

    private fun rollDice() {

        val randomInt = Random().nextInt(6) + 1
        val drawableResource = when (randomInt) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        image_dice.setImageResource(drawableResource)

    }
}