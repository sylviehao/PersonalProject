package com.sylvie.boardgameguide.tools.dice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.R
import java.util.*

class DiceViewModel : ViewModel() {

    val amount = MutableLiveData<Int>()

    val text = MutableLiveData<Boolean>().apply {
        value == false
    }

    init {
        amount.value = 1
    }

    fun plusStock() {
        if (amount.value!! < 10) {
        amount.value = amount.value?.plus(1)
        Log.d("Bottom", "$amount")
        }
    }

    fun minusStock() {
        if (amount.value!! > 1) {
            amount.value = amount.value?.minus(1)
        }
    }

    fun rollDice(): Int {

        //        image_dice.setImageResource(drawableResource)
        return when (Random().nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }
}