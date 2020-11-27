package com.sylvie.boardgameguide.newPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sylvie.boardgameguide.databinding.DialogNewPostBinding

class NewPostDialog : DialogFragment() {

    private  lateinit var binding : DialogNewPostBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}