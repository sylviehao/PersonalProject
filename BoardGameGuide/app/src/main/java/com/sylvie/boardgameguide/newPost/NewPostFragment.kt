package com.sylvie.boardgameguide.newPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import kotlinx.android.synthetic.main.activity_main.*

class NewPostFragment : Fragment() {

//    private val viewModel by viewModels<NewPostViewModel> {  }
    private lateinit var binding : FragmentNewPostBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner






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