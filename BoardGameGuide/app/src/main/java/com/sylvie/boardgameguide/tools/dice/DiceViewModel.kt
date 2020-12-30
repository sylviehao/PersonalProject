package com.sylvie.boardgameguide.tools.dice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sylvie.boardgameguide.R
import java.util.*

class DiceViewModel : ViewModel() {

    val amount = MutableLiveData<Int>()

    //Let dices rotate together -> initialize
    val rotateStatus = MutableLiveData<Boolean>().apply {
        value == false
    }

    val totalAmount = MutableLiveData<Int>()

    init {
        amount.value = 1
    }

    fun plusStock() {
        if (amount.value!! < 9) {
            amount.value = amount.value?.plus(1)
            rotateStatus.value = false
            Log.d("Bottom", "$amount")
        }
    }

    fun minusStock() {
        if (amount.value!! > 1) {
            amount.value = amount.value?.minus(1)
        }
    }

    fun getRandomNumber(): Int {
        //start number = 1, finish number = 6
        return Random().nextInt(6) + 1
    }

    fun getDiceImage(randomInt: Int): Int {
        val diceCount =
            mutableListOf(
                R.drawable.dice_grey_1,
                R.drawable.dice_grey_2,
                R.drawable.dice_grey_3,
                R.drawable.dice_grey_4,
                R.drawable.dice_grey_5,
                R.drawable.dice_grey_6
            )
        return diceCount[randomInt - 1]
    }
}