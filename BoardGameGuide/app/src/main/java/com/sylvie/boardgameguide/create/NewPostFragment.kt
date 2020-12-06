package com.sylvie.boardgameguide.create

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
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

class NewPostFragment : Fragment() {


    private lateinit var binding : FragmentNewPostBinding
    val viewModel by viewModels<NewPostViewModel> { getVmFactory() }

    // Separate the situation from HomeFragment and from GameFragment
    var arg: Game? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        arg = NewPostFragmentArgs.fromBundle(requireArguments()).game

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }


        arg?.let {
            binding.editNewPostGameName.setText(it.name)
            binding.editNewPostGameType.setText(it.type.toString())
            binding.editNewPostGameRule.setText(it.rules)
            binding.editNewPostGameMember.setText(it.playerLimit.toString())
        }

        val db = FirebaseFirestore.getInstance()
        binding.buttonNewPostCreate.setOnClickListener {

            val data = Event(
                hostId = "taiyilin",
                topic = binding.editNewPostTopic.text.toString(),
                description = "",
                image = mutableListOf("https://images.unsplash.com/photo-1506654020181-7c2ef87cc5a9?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MzZ8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
                "https://images.unsplash.com/photo-1431207446535-a9296cf995b1?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MzJ8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
                "https://images.unsplash.com/photo-1529488127598-5b1512330f89?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxzZWFyY2h8Mzd8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&auto=format&fit=crop&w=600&q=60"),
                time = binding.editNewPostGameTime.text.toString().toLong(),
                location = binding.editNewPostGameLocation.text.toString(),
                gameId = "a003",
                message = mutableListOf(Message(
                   userId = "",
                    message = ""
                )),
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