package com.sylvie.boardgameguide.newPost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class NewPostFragment : Fragment() {

//    private val viewModel by viewModels<NewPostViewModel> {  }
    private lateinit var binding : FragmentNewPostBinding
    val viewModel by viewModels<NewPostViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }


        val db = FirebaseFirestore.getInstance()
        binding.buttonNewPostCreate.setOnClickListener {

            val data = Event(
                hostId = "sylviehao",
                topic = binding.editNewPostTopic.text.toString(),
                description = "跨年夜聚會!",
                image = mutableListOf("https://images.unsplash.com/photo-1563811771046-ba984ff30900?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MjB8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
                "https://images.unsplash.com/photo-1599641499370-f4338ba82a04?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NDF8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
                "https://images.unsplash.com/photo-1533049883418-f0e0a8a39576?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NTZ8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60"),
                time = binding.editNewPostGameTime.text.toString().toLong(),
                location = binding.editNewPostGameLocation.text.toString(),
                gameId = "a005",
                rules = binding.editNewPostGameRule.text.toString(),
                playerList = mutableListOf("sylviehao", "Louis", "Taiyi", "Gary"),
                status = "CLOSE"
            )

            // Add a new document with a generated ID
            db.collection("Event")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    documentReference.update("id", documentReference.id)
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener {
                    Log.d("TAG", "$it")
                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
                        .show()
                }
            findNavController().navigate(R.id.action_global_homeFragment)
        }





        return binding.root
    }

    override fun onStart() {
        super.onStart()
        let {
//            requireActivity().toolbar.visibility = View.GONE
            requireActivity().bottomNavView.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        let {
//            requireActivity().toolbar.visibility = View.VISIBLE
            requireActivity().bottomNavView.visibility = View.VISIBLE
        }
    }
}