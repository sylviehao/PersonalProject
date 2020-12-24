package com.sylvie.boardgameguide.tools.timer

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        readData()

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

        binding.button1.setOnClickListener {
            resetCount(binding.text1.text.toString())
        }

        binding.button2.setOnClickListener {
            resetCount(binding.text2.text.toString())
        }

        binding.button3.setOnClickListener {
            resetCount(binding.text3.text.toString())
        }

        binding.buttonCustom.setOnClickListener {
            showDialog()
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
        if(timerStatus){
            timer.cancel()
            timerStatus = false
        }
        binding.circularProgressBar.progress = 0F
        binding.textTime.text = time
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.tools_timer_edit)
        val edit1 = dialog.findViewById(R.id.edit_1) as EditText
        val edit2 = dialog.findViewById(R.id.edit_2) as EditText
        val edit3 = dialog.findViewById(R.id.edit_3) as EditText
        val textSend = dialog.findViewById(R.id.text_send) as TextView

        textSend.setOnClickListener {
            binding.text1.text = edit1.text.toString()
            binding.text2.text = edit2.text.toString()
            binding.text3.text = edit3.text.toString()
            saveData(edit1.text.toString(), edit2.text.toString(), edit3.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun readData() {
        val settings = requireContext().getSharedPreferences("Timer", 0)
        binding.text1.text = (settings.getString("time1", "30"))
        binding.text2.text = (settings.getString("time2", "60"))
        binding.text3.text = (settings.getString("time3", "90"))
    }

    private fun saveData(time1: String, time2: String, time3: String) {
        val settings = requireContext().getSharedPreferences("Timer", 0)
        settings.edit()
            .putString("time1", time1)
            .putString("time2", time2)
            .putString("time3", time3)
            .apply()
    }
}