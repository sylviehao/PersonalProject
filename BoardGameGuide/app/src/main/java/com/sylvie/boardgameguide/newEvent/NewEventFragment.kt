package com.sylvie.boardgameguide.newEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentNewEventBinding
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import kotlinx.android.synthetic.main.activity_main.*

class NewEventFragment : Fragment() {

    //    private val viewModel by viewModels<NewPostViewModel> {  }
    private lateinit var binding : FragmentNewEventBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonNewEventCreate.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }





        return binding.root
    }

    override fun onStart() {
        super.onStart()
        let {
            requireActivity().toolbar.visibility = View.GONE
            requireActivity().bottomNavView.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        let {
            requireActivity().toolbar.visibility = View.VISIBLE
            requireActivity().bottomNavView.visibility = View.VISIBLE
        }
    }
}